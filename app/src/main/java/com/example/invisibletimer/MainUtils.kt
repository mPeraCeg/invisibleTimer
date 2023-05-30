package com.example.invisibletimer

import android.content.res.Resources
import androidx.annotation.VisibleForTesting
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        // TODO: Check all the date
        // Return true if the current and the last date is the same
        return lastDate.split('/')[0] == currentDate.split('/')[0]
    }

    fun calculateTimeRemainingToMidnight(startTime: String): Double {
        val midnightCalendar = Calendar.getInstance()
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 23)
        midnightCalendar.set(Calendar.MINUTE, 59)
        midnightCalendar.set(Calendar.SECOND, 59)

        val diffSeconds = (midnightCalendar.time.time - formatStringToDate(startTime).time)/1000
        return diffSeconds.toDouble()
    }

    fun getSwitchButtonMessage(on: Boolean): String {
        var message = Resources.getSystem().getString(R.string.stop_counting)
        if (on) message = Resources.getSystem().getString(R.string.start_counting)
        return message
    }

    fun formatDateToString(date: Date, formatValue: String): String {
        val format = SimpleDateFormat(formatValue, Locale.getDefault())
        return format.format(date)
    }

    fun formatStringToDate(date: String): Date {
        val format = SimpleDateFormat(STRINGS.FULL_DATE_FORMAT.value,
            Locale.getDefault())
        return format.parse(date)
    }

    fun getDayTotalTime(currentDate: Date, dbHelper: MyDatabaseHelper): Double {
        val listTotalTime = dbHelper.getTimersDay(formatDateToString(currentDate,
            STRINGS.DAY_DATE_FORMAT.value))
        return listTotalTime.sum()
    }
}
