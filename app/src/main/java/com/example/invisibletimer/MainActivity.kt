package com.example.invisibletimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.time.*
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    var on: Boolean = false
    var lastId: Int = 0
    var currentDate: Date = Date()

    val mainUtils = MainUtils()
    val databaseUtils = DatabaseUtils()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load data
        val dbHelper = MyDatabaseHelper(this)
        setup(dbHelper)

        val putButton = findViewById<Button>(R.id.switchButton)
        putButton.setOnClickListener {
            // Button click event handler
            on = if (on) {
                clickPutOffButton(dbHelper)
                false
            } else {
                clickPutOnButton(dbHelper)
                true
            }

            // Display a toast message
            Toast.makeText(this@MainActivity,
                getSwitchButtonMessage(on), Toast.LENGTH_SHORT).show()
        }

        val historyButton = findViewById<Button>(R.id.historyButton)
        historyButton.setOnClickListener {
            // Button click event handler TODO

            // Display a toast message
            Toast.makeText(this@MainActivity,
                getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

        val metricsButton = findViewById<Button>(R.id.metricsButton)
        metricsButton.setOnClickListener {
            // Button click event handler TODO

            // Display a toast message
            Toast.makeText(this@MainActivity,
                getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n", "VisibleForTests")
    private fun setup(dbHelper: MyDatabaseHelper) {
        // 1. Load last timer
        val timers = dbHelper.getAllTimers()
        if (timers.isEmpty()) {
            databaseUtils.insertTimer(dbHelper, 0, formatDateToString(Calendar.getInstance().time, getString(R.string.day_date_format)),
                formatDateToString(Calendar.getInstance().time, getString(R.string.full_date_format)))
        }
        var lastTimer = timers.last()

        // 2. Update global variables
        lastId = lastTimer.id
        on = lastTimer.end_time.isEmpty()

        // 3. Check if the day change
        if (on && !mainUtils.sameDate(lastTimer.date, formatDateToString(currentDate, getString(R.string.day_date_format)))) {
            val remainingTimeToMidnightSeconds = calculateTimeRemainingToMidnight(lastTimer.start_time)
            databaseUtils.updateDateTimer(dbHelper, lastTimer, lastTimer.start_time.split(" ")[0] + " 23:59:59", remainingTimeToMidnightSeconds)
            databaseUtils.insertTimer(dbHelper, lastId, formatDateToString(Calendar.getInstance().time, getString(R.string.day_date_format)),
                formatDateToString(Calendar.getInstance().time, getString(R.string.full_date_format)).split(" ")[0] + " 00:00:01")

            lastTimer = dbHelper.getLastTimer()
            lastId = lastTimer.id
        }

        // 4. Show the actual status in the UI
        val currentDateText = findViewById<TextView>(R.id.currentDateTextView)
        currentDateText.text = formatDateToString(currentDate, getString(R.string.day_date_format))
        val timerText = findViewById<TextView>(R.id.totalTimeTextView)

        if (on) {
            val putButton = findViewById<Button>(R.id.switchButton)
            putButton.text = getString(R.string.put_off)
            val statusCircle = findViewById<ImageView>(R.id.statusCircle)
            statusCircle.setImageResource(R.drawable.baseline_circle_24_green)
            val lastTimerStartTimeDate = formatStringToDate(lastTimer.start_time)
            timerText.text = mainUtils.secondsToReadableFormat(mainUtils.calculateTotalTimer(lastTimerStartTimeDate, currentDate).toString(), true)
        } else {
            timerText.text = getString(R.string.zero_time)
        }
    }

    private fun clickPutOnButton(dbHelper: MyDatabaseHelper) {
        // Insert timer
        databaseUtils.insertTimer(dbHelper, lastId, formatDateToString(Calendar.getInstance().time, getString(R.string.day_date_format)),
            formatDateToString(Calendar.getInstance().time, getString(R.string.full_date_format)))
        val lastTimer = dbHelper.getLastTimer()
        lastId = lastTimer.id

        // Update the UI
        val putButton = findViewById<Button>(R.id.switchButton)
        putButton.text = getString(R.string.put_off)
        val statusCircle = findViewById<ImageView>(R.id.statusCircle)
        statusCircle.setImageResource(R.drawable.baseline_circle_24_green)
    }

    private fun clickPutOffButton(dbHelper: MyDatabaseHelper) {
        // Update timer
        val lastTimer = dbHelper.getLastTimer()
        val totalTime = mainUtils.calculateTotalTimer(formatStringToDate(lastTimer.start_time), Calendar.getInstance().time)
        databaseUtils.updateDateTimer(dbHelper, lastTimer,
            formatDateToString(Calendar.getInstance().time, getString(R.string.full_date_format)),
            totalTime)

        // Sum all the times in a day
        val totalSumTime = getDayTotalTime(dbHelper)

        // Update the UI
        val putButton = findViewById<Button>(R.id.switchButton)
        putButton.text = getString(R.string.put_on)
        val totalTimeText = findViewById<TextView>(R.id.totalTimeTextView)
        totalTimeText.text = mainUtils.secondsToReadableFormat(totalSumTime.toString(), true)
        val statusCircle = findViewById<ImageView>(R.id.statusCircle)
        statusCircle.setImageResource(R.drawable.baseline_circle_24_red)
    }

    private fun calculateTimeRemainingToMidnight(startTime: String): Double {
        val midnightCalendar = Calendar.getInstance()
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 23)
        midnightCalendar.set(Calendar.MINUTE, 59)
        midnightCalendar.set(Calendar.SECOND, 59)

        val diffSeconds = (midnightCalendar.time.time - formatStringToDate(startTime).time)/1000
        return diffSeconds.toDouble()
    }

    private fun getSwitchButtonMessage(on: Boolean): String {
        var message = getString(R.string.stop_counting)
        if (on) message = getString(R.string.start_counting)
        return message
    }

    private fun formatDateToString(date: Date, formatValue: String): String {
        val format = SimpleDateFormat(formatValue, Locale.getDefault())
        return format.format(date)
    }

    private fun formatStringToDate(date: String): Date {
        val format = SimpleDateFormat(getString(R.string.full_date_format), Locale.getDefault())
        return format.parse(date)
    }

    private fun getDayTotalTime(dbHelper: MyDatabaseHelper): Double {
        val listTotalTime = dbHelper.getTimersDay(formatDateToString(currentDate,
            getString(R.string.day_date_format)))
        return listTotalTime.sum()
    }
}
