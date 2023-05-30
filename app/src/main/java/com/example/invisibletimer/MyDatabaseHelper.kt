package com.example.invisibletimer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "timer_final.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "saved_timer"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_START = "start_time"
        private const val COLUMN_END = "end_time"
        private const val COLUMN_TOTAL = "total"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_DATE TEXT, $COLUMN_START TEXT, $COLUMN_END TEXT, $COLUMN_TOTAL DOUBLE)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTimer(timer: Timer) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, timer.date)
        values.put(COLUMN_START, timer.start_time)
        values.put(COLUMN_END, timer.end_time)
        values.put(COLUMN_TOTAL, timer.total)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateTimer(timer: Timer) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, timer.date)
        values.put(COLUMN_START, timer.start_time)
        values.put(COLUMN_END, timer.end_time)
        values.put(COLUMN_TOTAL, timer.total)
        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(timer.id.toString()) )
        db.close()
    }

    @SuppressLint("Range")
    fun getAllTimers(): List<Timer> {
        val timerList = mutableListOf<Timer>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val start_time = cursor.getString(cursor.getColumnIndex(COLUMN_START))
                val end_time = cursor.getString(cursor.getColumnIndex(COLUMN_END))
                val total = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL))
                val timer = Timer(id, date, start_time, end_time, total)
                timerList.add(timer)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return timerList
    }

    @SuppressLint("Range")
    fun deleteTimer(id: Int) {
        val db = readableDatabase
        val deleteQuery = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = $id"
        db.execSQL(deleteQuery)
    }

    @SuppressLint("Range")
    fun deleteAllTimers() {
        val db = readableDatabase
        val deleteQuery = "DELETE FROM $TABLE_NAME"
        db.execSQL(deleteQuery)
    }

    @SuppressLint("Range")
    fun getLastTimer(): Timer {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = (SELECT MAX($COLUMN_ID) FROM $TABLE_NAME)"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
        val start_time = cursor.getString(cursor.getColumnIndex(COLUMN_START))
        val end_time = cursor.getString(cursor.getColumnIndex(COLUMN_END))
        val total = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL))
        val timer = Timer(id, date, start_time, end_time, total)
        cursor.close()
        db.close()
        return timer
    }

    @SuppressLint("Range")
    fun getTimersDay(dayDate: String): List<Double> {
        val totalTimesList = mutableListOf<Double>()
        val db = readableDatabase
        val selectQuery = "SELECT $COLUMN_TOTAL FROM $TABLE_NAME WHERE $COLUMN_DATE=\"$dayDate\""
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                totalTimesList.add(cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return totalTimesList
    }
}
