package org.ieselcaminas.victor.minesweeper2019

import android.os.Handler
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SweeperTimer(lifecycle: Lifecycle, val timerText: TextView): LifecycleObserver{

    // The number of seconds counted since the timer started
    var secondsCount = 0

    /**
     * [Handler] is a class meant to process a queue of messages (known as [android.os.Message]s) or actions (known as [Runnable]s)
     */

    private var handler = Handler()
    private lateinit var runnable: Runnable

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer() {
        runnable = Runnable {
            secondsCount++
            timerText.text = secondsCount.toString()

            handler.postDelayed(runnable, 1000)
        }

        // This is what initially starts the timer
        handler.postDelayed(runnable, 1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {

        handler.removeCallbacks(runnable)
    }
}