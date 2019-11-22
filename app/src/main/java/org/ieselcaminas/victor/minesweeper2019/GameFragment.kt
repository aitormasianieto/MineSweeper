package org.ieselcaminas.victor.minesweeper2019

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentGameBinding

interface FlagsInterface {
    fun addFlag()
    fun subFlag()
    fun canPutFlag(): Boolean
}


class GameFragment : Fragment(), FlagsInterface {

    lateinit var binding: FragmentGameBinding
    lateinit var board: Array<Array<MineButton>>
    lateinit var bombMatrix: BombMatrix
    lateinit var timer: SweeperTimer
    var flagsCounter = -1
    var numRows: Int = 0
    var numCols: Int = 0

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        val args = GameFragmentArgs.fromBundle(arguments!!)
        numRows = args.numRows
        numCols = args.numCols
        bombMatrix = BombMatrix(numRows, numCols, (numRows * numCols) / 6)

        flagsCounter = bombMatrix.numBombs
        binding.flagsTextView.text = flagsCounter.toString()

        timer = SweeperTimer(lifecycle, binding.timerTextView)

        Toast.makeText(context, "Rows = $numRows Cols = $numCols", Toast.LENGTH_LONG).show()

        createButtons()

        return binding.root
    }


    private fun createButtons() {
        board = Array(numRows) { row ->
            Array(numCols) { col ->
                MineButton(context!!, row, col, this)
            }
        }
        //EL GRID LAYOUT TE AÑADE LOS BOTONES EN VERTICAL EN VEZ DE EN HORIZONTAL
        binding.gridLayout.columnCount = numCols
        binding.gridLayout.rowCount = numRows

        for (col in 0..numCols-1) { //ENTONCES QUEREMOS QUE VAYAN AVANZANDO LAS ROWS DENTRO DE CADA UNA DE LAS COLS
            for (row in 0..numRows-1) {
                putsBackgroundAndButton(row, col)

                board[row][col].setOnClickListener() { it as MineButton

                    //watch row, col and result
                    println("row: ${it.row} col: ${it.col}" + "   " + bombMatrix.board[it.row][it.col])

                    if (board[row][col].state == StateType.CLOSED) { //A no ser que este en estado normal, cuando clicas no se abre
                        it.state = StateType.OPEN
                        it.visibility = View.INVISIBLE

                        //Abertura en caso de 0's
                        zeroOpener(it.row, it.col) //He insertado el checker de gameover aqui dentro para que sea recursivo e y evitar errores
                    }
                }
            }
        }
    }

    private fun gameOverChecker(inRow: Int, inCol: Int) {
        if (bombMatrix.board[inRow][inCol] == bombMatrix.BOMB_NUMBER) {
            makeResult(false)

            for (row in 0 until numRows) {
                for (col in 0 until numCols) {
                    if (bombMatrix.board[row][col] == bombMatrix.BOMB_NUMBER) {
                        if (board[row][col].state == StateType.FLAG) {
                            board[row][col].imgBackground.setImageResource(R.drawable.defused_bomb)
                        }
                        else {
                            board[row][col].imgBackground.setImageResource(R.drawable.bomb_explode)
                        }
                        board[row][col].visibility = View.INVISIBLE
                        board[row][col].state = StateType.OPEN
                    }
                }
            }
            Log.i("GameFragment", "Game Over")
        }
        else {
            gameWinChecker()
        }
    }

    private fun gameWinChecker() {
        var win = true

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                if (bombMatrix.board[row][col] != bombMatrix.BOMB_NUMBER) {
                    if (board[row][col].state != StateType.OPEN) {
                        win = false
                    }
                }
            }
        }
        if (win) {
            makeResult(true)
        }
    }

    private fun makeResult(b: Boolean) {
        val result = binding.resultTextView
        timer.stopTimer()

        if (b) {
            result.text = "You Win!!"
            result.setTextColor(Color.BLUE)
            result.visibility = TextView.VISIBLE
        }
        else {
            result.text = "Game Over"
            result.setTextColor(Color.RED)
            result.visibility = TextView.VISIBLE
        }

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                board[row][col].isEnabled = false

                if (board[row][col].state == StateType.FLAG) {
                    board[row][col].imgBackground.setImageResource(R.drawable.defused_bomb)
                }

                board[row][col].visibility = View.INVISIBLE
                board[row][col].state = StateType.OPEN
            }
        }
    }

    private fun putsBackgroundAndButton(row: Int, col: Int) {
        val layout = FrameLayout(context!!)
        layout.layoutParams = FrameLayout.LayoutParams(75, 75)

        val imgBackground = ImageView(context!!)
        imgBackground.setImageResource(setImgNumber(row, col))
        board[row][col].imgBackground = imgBackground

        layout.addView(imgBackground)
        board[row][col].alpha = 0.7f //usado para transparentra los botones y pder testear mas facilmente
        layout.addView(board[row][col])

        binding.gridLayout.addView(layout)
    }

    private fun setImgNumber(row: Int, col:Int): Int {

        return when (bombMatrix.board[row][col]) {
            0 ->
                R.drawable.n0
            1 ->
                R.drawable.n1
            2 ->
                R.drawable.n2
            3 ->
                R.drawable.n3
            4 ->
                R.drawable.n4
            5 ->
                R.drawable.n5
            6 ->
                R.drawable.n6
            7 ->
                R.drawable.n7
            8 ->
                R.drawable.n8
            9 ->
                R.drawable.bomb
            else ->
                R.drawable.boton
        }
    }

    private fun zeroOpener(nRow: Int, nCol: Int) {
        gameOverChecker(nRow, nCol) //Lo he puesto aqui para poder hacer recursivo el metodo game over ya que cuando se abren los espacios vacios,
                                    // si los dejo para lo ultimo no me da win, ya que no detecta la victoria desde lel click
        if (bombMatrix.board[nRow][nCol] == 0) {
            for (row in nRow - 1..nRow + 1) {
                for (col in nCol - 1..nCol + 1) {
                    if (bombMatrix.isValid(row, col)) {
                        if (!(nRow == row && nCol == col)) {
                            if (board[row][col].state == StateType.CLOSED) {

                                board[row][col].state = StateType.OPEN
                                board[row][col].visibility = View.INVISIBLE

                                zeroOpener(row, col)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun addFlag() {
        flagsCounter--
        binding.flagsTextView.text = flagsCounter.toString()
    }

    override fun subFlag() {
        flagsCounter++
        binding.flagsTextView.text = flagsCounter.toString()
    }

    override fun canPutFlag(): Boolean {
        return flagsCounter == 0
    }
}