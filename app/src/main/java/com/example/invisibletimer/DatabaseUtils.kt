package com.example.invisibletimer

class DatabaseUtils {

    fun insertTimer(dbHelper: MyDatabaseHelper, lastId: Int, date: String, start_time: String) {
        dbHelper.insertTimer(Timer(
            id = lastId+1,
            date = date,
            start_time = start_time,
            end_time = "",
            total = 0.0))
    }

    fun updateDateTimer(dbHelper: MyDatabaseHelper, timer: Timer, endTime: String, totalTime: Double) {
        dbHelper.updateTimer(Timer(
            id = timer.id,
            date = timer.date,
            start_time = timer.start_time,
            end_time = endTime,
            total = totalTime))
    }

}