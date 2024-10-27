package com.map.hung.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.GridView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.adapter.CalendarAdapter
import java.util.*

class MonthFragment : Fragment() {

    private var calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)

        val gridView = view.findViewById<GridView>(R.id.calendar_grid)

        // Generate days of the current month
        val daysOfMonth = generateDaysOfMonth(calendar)
        val adapter = CalendarAdapter(requireContext(), daysOfMonth)
        gridView.adapter = adapter

        return view
    }

    companion object {
        // Create a new instance of MonthFragment with the specific month and year
        fun newInstance(month: Int, year: Int): MonthFragment {
            val fragment = MonthFragment()
            val args = Bundle()
            args.putInt("month", month)
            args.putInt("year", year)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val month = it.getInt("month")
            val year = it.getInt("year")
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
        }
    }

    private fun generateDaysOfMonth(calendar: Calendar): List<Date> {
        val days = mutableListOf<Date>()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek)
        for (i in 0 until firstDayOfWeek) {
            days.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            days.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return days
    }
}
