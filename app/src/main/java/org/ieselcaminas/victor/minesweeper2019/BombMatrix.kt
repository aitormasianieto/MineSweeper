package org.ieselcaminas.victor.minesweeper2019

import android.opengl.ETC1.isValid
import kotlin.random.Random

class BombMatrix(val numRows: Int, val numCols: Int, var numBombs: Int) {

    var board: Array<Array<Int>>
    val BOMB_NUMBER = 9

    init {

        board = Array(numRows) { row ->
            Array(numCols) { col ->
                0
            }
        }
        createBombs(numBombs)

        checkNums()
    }

    private fun createBombs(bombs: Int) {
        var counter = 0
        while (counter < bombs) {
            var row = Random.nextInt(0, numRows-1)
            var col = Random.nextInt(0, numCols-1)

            if (board[row][col] != BOMB_NUMBER) {
                board[row][col] = BOMB_NUMBER
                calculateNumbers(row, col)
                counter++
            }
        }
    }

    private fun calculateNumbers(nRow: Int, nCol: Int) {

        for (row in nRow-1..nRow+1) {
            for (col in nCol-1..nCol+1) {
                if (isValid(row, col)) {
                    if (!(nRow == row && nCol == col)) {
                        board[row][col] += 1
                    }
                }
            }
        }
    }

    fun isValid(row: Int, col: Int): Boolean {

        if (row == -1 || col == -1 || row == numRows || col == numCols || board[row][col] == BOMB_NUMBER) {
            return false
        }
        return true
    }

    //imprime en el LogCat los numero en cada posicion
    private fun checkNums() {
        for (row in board) {
            for (col in row) {
                print("$col ")
            }
            println()
        }
    }
}