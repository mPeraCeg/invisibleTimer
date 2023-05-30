package com.example.invisibletimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.time.*
import java.util.Calendar
import java.util.Date

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
                mainUtils.getSwitchButtonMessage(on), Toast.LENGTH_SHORT).show()
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
            databaseUtils.insertTimer(dbHelper, 0, mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.DAY_DATE_FORMAT.value),
                mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.FULL_DATE_FORMAT.value))
        }
        var lastTimer = timers.last()

        // 2. Update global variables
        lastId = lastTimer.id
        on = lastTimer.end_time.isEmpty()

        // 3. Check if the day change
        if (on && !mainUtils.sameDate(lastTimer.date, mainUtils.formatDateToString(currentDate, STRINGS.DAY_DATE_FORMAT.value))) {
            val remainingTimeToMidnightSeconds = mainUtils.calculateTimeRemainingToMidnight(lastTimer.start_time)
            databaseUtils.updateDateTimer(dbHelper, lastTimer, lastTimer.start_time.split(" ")[0] + " 23:59:59", remainingTimeToMidnightSeconds)
            databaseUtils.insertTimer(dbHelper, lastId, mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.DAY_DATE_FORMAT.value),
                mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.FULL_DATE_FORMAT.value).split(" ")[0] + " 00:00:01")

            lastTimer = dbHelper.getLastTimer()
            lastId = lastTimer.id
        }

        // 4. Show the actual status in the UI
        val currentDateText = findViewById<TextView>(R.id.currentDateTextView)
        currentDateText.text = mainUtils.formatDateToString(currentDate, STRINGS.DAY_DATE_FORMAT.value)
        val timerText = findViewById<TextView>(R.id.totalTimeTextView)

        if (on) {
            val putButton = findViewById<Button>(R.id.switchButton)
            putButton.text = getString(R.string.put_off)
            val statusCircle = findViewById<ImageView>(R.id.statusCircle)
            statusCircle.setImageResource(R.drawable.baseline_circle_24_green)
            val lastTimerStartTimeDate = mainUtils.formatStringToDate(lastTimer.start_time)
            timerText.text = mainUtils.secondsToReadableFormat(mainUtils.calculateTotalTimer(lastTimerStartTimeDate, currentDate).toString(), true)
        } else {
            timerText.text = getString(R.string.zero_time)
        }
    }

    private fun clickPutOnButton(dbHelper: MyDatabaseHelper) {
        // Insert timer
        databaseUtils.insertTimer(dbHelper, lastId, mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.DAY_DATE_FORMAT.value),
            mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.FULL_DATE_FORMAT.value))
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
        val totalTime = mainUtils.calculateTotalTimer(mainUtils.formatStringToDate(lastTimer.start_time), Calendar.getInstance().time)
        databaseUtils.updateDateTimer(dbHelper, lastTimer,
            mainUtils.formatDateToString(Calendar.getInstance().time, STRINGS.FULL_DATE_FORMAT.value),
            totalTime)

        // Sum all the times in a day
        val totalSumTime = mainUtils.getDayTotalTime(currentDate, dbHelper)

        // Update the UI
        val putButton = findViewById<Button>(R.id.switchButton)
        putButton.text = getString(R.string.put_on)
        val totalTimeText = findViewById<TextView>(R.id.totalTimeTextView)
        totalTimeText.text = mainUtils.secondsToReadableFormat(totalSumTime.toString(), true)
        val statusCircle = findViewById<ImageView>(R.id.statusCircle)
        statusCircle.setImageResource(R.drawable.baseline_circle_24_red)
    }

}
