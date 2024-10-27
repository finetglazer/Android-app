package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.CategoryDao
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.map.hung.myapplication.dao.StatDao

class StatTimeAct : Activity() {

    private lateinit var periodSpinner: Spinner
    private lateinit var barChart: BarChart
    private lateinit var catList: LinearLayout
    private lateinit var statDao: StatDao
    private lateinit var backTxt: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_stat)

        periodSpinner = findViewById(R.id.periodSpinner)
        barChart = findViewById(R.id.barChart)
        catList = findViewById(R.id.timeList)
        backTxt = findViewById(R.id.back)

        statDao = StatDao(this)

        val options = arrayOf("By Month", "By Year")
        periodSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    updateChart(statDao.getMonthlyStatistics(), "Month")
                } else {
                    updateChart(statDao.getYearlyStatistics(), "Year")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        backTxt.setOnClickListener {
            val intent = Intent(this, HomeAct::class.java)
            startActivity(intent)
        }
    }

    private fun updateChart(data: List<Triple<String, Double, Double>>, labelType: String) {
        val barEntries = mutableListOf<BarEntry>()
        val yValues = mutableListOf<Float>()

        for ((index, triple) in data.withIndex()) {
            val (label, income, outcome) = triple
            val incomeValue = income.toFloat()
            val outcomeValue = -outcome.toFloat() // Negative for outcome if you want it displayed below zero

            // Create a stacked BarEntry with both income and outcome
            barEntries.add(BarEntry(index.toFloat(), floatArrayOf(incomeValue, outcomeValue)))

            // Collect Y-values for axis scaling
            yValues.add(incomeValue)
            yValues.add(outcomeValue)
        }

        // Create a single BarDataSet with stack labels
        val barDataSet = BarDataSet(barEntries, "").apply {
            setColors(Color.GREEN, Color.RED)
            stackLabels = arrayOf("Income", "Outcome")
            // Set custom value formatter
            valueFormatter = object : ValueFormatter() {
                override fun getBarStackedLabel(value: Float, stackEntry: BarEntry?): String {
                    // If both income and outcome are zero, display a single zero or hide the label
                    val stackValues = stackEntry?.yVals
                    if (stackValues != null) {
                        val incomeVal = stackValues[0]
                        val outcomeVal = stackValues[1]
                        if (incomeVal == 0f && outcomeVal == 0f) {
                            return "0" // Display a single zero
                        }
                    }
                    // Return empty string for zero values to avoid overlapping labels
                    return if (value == 0f) "" else value.toInt().toString()
                }
            }
        }

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f // Adjust bar width as needed

        barChart.data = barData

        // Calculate the Y-axis minimum and maximum based on data
        val yMin = yValues.minOrNull() ?: 0f
        val yMax = yValues.maxOrNull() ?: 0f
        val yAxisRange = yMax - yMin
        val yAxisPadding = if (yAxisRange == 0f) yMax * 0.1f else yAxisRange * 0.1f

        // Adjust axis to be flexible
        barChart.axisLeft.axisMinimum = yMin - yAxisPadding
        barChart.axisLeft.axisMaximum = yMax + yAxisPadding
        barChart.axisRight.axisMinimum = yMin - yAxisPadding
        barChart.axisRight.axisMaximum = yMax + yAxisPadding

        // Customize the X-axis labels
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.first })
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.isGranularityEnabled = true

        // Enable zooming and panning (optional)
        barChart.setScaleEnabled(true)
        barChart.setPinchZoom(true)
        barChart.isDoubleTapToZoomEnabled = true
        barChart.isDragEnabled = true

        // Customize the legend
        barChart.legend.isEnabled = true
        barChart.legend.form = Legend.LegendForm.SQUARE
        barChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        barChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        barChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL

        // Refresh the chart
        barChart.invalidate()

        updateCatList(data, labelType)
    }


    private fun updateCatList(data: List<Triple<String, Double, Double>>, labelType: String) {
        catList.removeAllViews()

        for ((label, income, outcome) in data) {
            val timeView = layoutInflater.inflate(R.layout.time_item, catList, false)
            val incomeText = timeView.findViewById<TextView>(R.id.IdIncome)
            val outcomeText = timeView.findViewById<TextView>(R.id.IdOutcome)
            val dateLabel = timeView.findViewById<TextView>(R.id.IdMonthYear)

            dateLabel.text = label
            incomeText.text = if (income >= 0) "$income" else "0.0"
            outcomeText.text = if (outcome >= 0) "$outcome" else "0.0"

            catList.addView(timeView)
        }
    }

}