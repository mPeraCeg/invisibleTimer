package com.example.invisibletimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity

class HistoryActivity : ComponentActivity() {

    val databaseUtils = DatabaseUtils()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic)
        val dbHelper = DatabaseManager.getDatabaseHelper(this)

        val items = databaseUtils.getHistoryTimers(dbHelper)

        val adapter = ListHistoricAdapter(this, R.layout.list_historic_item, items)
        val listHistoricView = findViewById<ListView>(R.id.list_historic_view)
        listHistoricView.adapter = adapter
    }

    // Data class representing an item
    data class HistoricEntry(val date: String, val timer: String, val statusImageId: Int)

}