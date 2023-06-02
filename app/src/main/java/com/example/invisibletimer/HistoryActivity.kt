package com.example.invisibletimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity

class HistoryActivity : ComponentActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic)

        val listHistoricView = findViewById<ListView>(R.id.list_historic_view)

        // Sample data
        val items = listOf(
            HistoricEntry("24/03/1999", "00h00m", R.drawable.baseline_circle_24_red),
            HistoricEntry("01/01/2010", "01h04m", R.drawable.baseline_circle_24_green),
            HistoricEntry("16/06/2015", "10h30m", R.drawable.baseline_circle_24_red),
            HistoricEntry("24/03/1993", "22h00m", R.drawable.baseline_circle_24_green),
            HistoricEntry("25/12/2021", "12h37m", R.drawable.baseline_circle_24_red)
        )


        val adapter = ListHistoricAdapter(this, R.layout.list_historic_item, items)
        listHistoricView.adapter = adapter

    }

    private fun getHistoricTimers() {
        //TODO: Get the timers from database
    }

    // Data class representing an item
    data class HistoricEntry(val date: String, val timer: String, val statusImageId: Int)

}