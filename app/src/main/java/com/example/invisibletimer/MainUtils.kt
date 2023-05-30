package com.example.invisibletimer

import androidx.annotation.VisibleForTesting
import java.util.Date

class MainUtils {

    @VisibleForTesting
    fun secondsToReadableFormat(seconds: String, ui: Boolean): String {
        val secondsFinal = (seconds.toDouble() % 60).toInt()
        val minutes = (seconds.toDouble() / 60).toInt()
        val minutesFinal = (minutes.toDouble() % 60).toInt()
        val hoursFinal = (minutes.toDouble() / 60).toInt()
        return if (ui) {
            hoursFinal.toString() + "h" + minutesFinal.toString() + "m" + secondsFinal.toString() + "s"
        } else {
            "$hoursFinal:$minutesFinal:$secondsFinal"
        }
    }

    fun calculateTotalTimer(startTimer: Date, endTimer: Date): Double {
        return ((endTimer.time - startTimer.time)/1000).toDouble()
    }

    fun sameDate(lastDate: String, currentDate: String): Boolean {
        // Return true if the current and the last date is the same
        return lastDate.split('/')[0] == currentDate.split('/')[0]
    }


}