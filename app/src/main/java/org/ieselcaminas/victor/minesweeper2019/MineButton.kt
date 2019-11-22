package org.ieselcaminas.victor.minesweeper2019

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable



class MineButton(context: Context, var row: Int, var col: Int, flagsInterf: FlagsInterface): ImageButton(context) {

    //var row: Int = row
    //var col: Int = col
    val SIZE = 75
    var state: StateType = StateType.CLOSED
    var imgBackground: ImageView = ImageView(context)

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
                button.state = StateType.CLOSED
            }
            else {
                if (button.state == StateType.FLAG) {
                    button.setImageDrawable(getDrawable(context, R.drawable.question))
                    button.state = StateType.QUESTION

                    flagsInterf.subFlag()
                }
                else {
                    if (button.state == StateType.CLOSED) {
                        if (!(flagsInterf.canPutFlag())) { //No poner banderas en caso de que este el numero maximo
                            button.setImageDrawable(getDrawable(context, R.drawable.flag))
                            button.state = StateType.FLAG

                            flagsInterf.addFlag()
                        }
                    }
                }
            }
            true
        }
    }

}