package org.ieselcaminas.victor.minesweeper2019

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import org.ieselcaminas.victor.minesweeper2019.databinding.FragmentGameBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    lateinit var binding: FragmentGameBinding
    lateinit var board: Array<Array<MineButton>>
    lateinit var bombMatrix: BombMatrix
    var numRows: Int = 0
    var numCols: Int = 0

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        binding.buttonWin.setOnClickListener() {
            it.findNavController().navigate(GameFragmentDirections.actionGameFragmentToWonFragment())
        }

        binding.buttonLose.setOnClickListener() {
            it.findNavController().navigate(GameFragmentDirections.actionGameFragmentToLoseFragment())
        }

        val args = GameFragmentArgs.fromBundle(arguments!!)
        numRows = args.numRows
        numCols = args.numCols
        bombMatrix = BombMatrix(numRows, numCols, (numRows * numCols) / 6)
        Toast.makeText(context, "Rows = $numRows Cols = $numCols", Toast.LENGTH_LONG).show()

        createButtons()

        return binding.root
    }


    private fun createButtons() {
        board = Array(numRows) { row ->
            Array(numCols) { col ->
                MineButton(context!!, row, col)
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

                    it.state = StateType.OPEN
                    it.visibility = View.INVISIBLE

                    gameOverChecker(it.row, it.col)

                    //Abertura en caso de 0's
                    zeroOpener(it.row, it.col)
                }
            }
        }
    }

    private fun gameOverChecker(inRow: Int, inCol: Int) {
        if (bombMatrix.board[inRow][inCol] == bombMatrix.BOMB_NUMBER) {
            for (row in 0..numRows-1) {
                for (col in 0..numCols-1) {
                    if (bombMatrix.board[row][col] == bombMatrix.BOMB_NUMBER) {
                        if (board[row][col].state == StateType.FLAG)

                        board[row][col].visibility = View.INVISIBLE
                        board[row][col].state = StateType.OPEN
                    }
                    Log.i("GameFragment", "Game Over")
                }
            }
        }
    }

    private fun putsBackgroundAndButton(row: Int, col: Int) {
        val layout = FrameLayout(context!!)
        layout.layoutParams = FrameLayout.LayoutParams(75, 75)

        val imgBackground = ImageView(context!!)
        imgBackground.setImageResource(setImgNumber(row, col))

        layout.addView(imgBackground)
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
}