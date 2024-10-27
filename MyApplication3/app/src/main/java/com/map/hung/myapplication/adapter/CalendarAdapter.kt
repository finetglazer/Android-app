package com.map.hung.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.map.hung.myapplication.R
import java.util.*

class CalendarAdapter(private val context: Context, private val daysOfMonth: List<Date>) : BaseAdapter() {

    override fun getCount(): Int {
        return daysOfMonth.size
    }

    override fun getItem(position: Int): Any {
        return daysOfMonth[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.calendar_day, parent, false)

        val dayTextView = view.findViewById<TextView>(R.id.calendar_day_text)

        // Get the day number from the Date object
        val day = Calendar.getInstance().apply { time = daysOfMonth[position] }.get(Calendar.DAY_OF_MONTH)

        // Set the day number on the TextView
        dayTextView.text = day.toString()

        return view
    }
}
