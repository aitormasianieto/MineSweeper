package org.ieselcaminas.victor.minesweeper2019

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable


class MineButton(context: Context, var row: Int, var col: Int): ImageButton(context) {

    //var row: Int = row
    //var col: Int = col
    val SIZE = 75
    public var state: StateType = StateType.CLOSED

    init {
        layoutParams = LinearLayout.LayoutParams(SIZE, SIZE)

        setPadding(0, 0, 0, 0)
        scaleType = ScaleType.CENTER
        adjustViewBounds = true


        setBackground(getDrawable(context, R.drawable.boton))

        setOnTouchListener() { view: View, event: MotionEvent ->
            val button: MineButton = view as MineButton

            if (event.action == MotionEvent.ACTION_DOWN) {
                button.background = getDrawable(context, R.drawable.boton_pressed)
            }
            else {
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                        button.background = getDrawable(context, R.drawable.boton)
                }
            }
            false
        }

        setOnLongClickListener() { view: View ->
            val button: MineButton = view as MineButton

            if (button.state == StateType.QUESTION) {
                button.setImageDrawable(null)
                state = StateType.CLOSED
            }
            else {
                if (button.state == StateType.FLAG) {
                    button.setImageDrawable(getDrawable(context, R.drawable.question))
                    state = StateType.QUESTION
                }
                else {
                    button.setImageDrawable(getDrawable(context, R.drawable.flag))
                    state = StateType.FLAG
                }
            }
            true
        }
    }

}