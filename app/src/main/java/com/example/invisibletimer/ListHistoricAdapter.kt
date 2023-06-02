package com.example.invisibletimer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ListHistoricAdapter(private val context: Context, private val layout: Int, private val entry: List<HistoryActivity.HistoricEntry>) : BaseAdapter()  {

    override fun getCount(): Int {
        return entry.size
    }

    override fun getItem(position: Int): Any {
        return entry[position]
    }

    override fun getItemId(id: Int): Long {
        return id.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        var v = convertView

        val layoutInflater = LayoutInflater.from(context)

        v = layoutInflater.inflate(R.layout.list_historic_item, null)

        val dateText = entry[position].date
        val timerText = entry[position].timer
        val statusView = entry[position].statusImageId

        val historicDateText = v.findViewById<TextView>(R.id.historicDateText)
        historicDateText.text = dateText
        val historicTimerText = v.findViewById<TextView>(R.id.historicTimerText)
        historicTimerText.text = timerText
        val historicStatusView = v.findViewById<ImageView>(R.id.historicStatusView)
        historicStatusView.setImageResource(statusView)

        return v
    }
}