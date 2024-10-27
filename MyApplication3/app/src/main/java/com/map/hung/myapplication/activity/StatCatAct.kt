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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.map.hung.myapplication.dao.StatDao

class StatCatAct : Activity() {
    private lateinit var periodSpinner: Spinner
    private lateinit var pieChart: PieChart
    private lateinit var catList: LinearLayout
    private lateinit var incomeRadioButton: RadioButton
    private lateinit var expenseRadioButton: RadioButton
    private lateinit var backTxt: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.cat_stat) // Assuming the XML layout file is named stat_cat.xml

        periodSpinner = findViewById(R.id.periodSpinner)
        pieChart = findViewById(R.id.pieChart)
        catList = findViewById(R.id.catList)
        incomeRadioButton = findViewById(R.id.income)
        expenseRadioButton = findViewById(R.id.expense)
        backTxt = findViewById(R.id.back)


        //set up spinner
        val listItem = arrayOf("Tháng hiện tại", "Tháng trước", "Năm hiện tại", "Năm ngoái")


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listItem)
        periodSpinner.adapter = adapter

        periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateCategoryListAndChart(incomeRadioButton.isChecked)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }




        // Inside StatCatAct.kt, within onCreate() or a similar setup method
        incomeRadioButton.setOnClickListener {
            updateCategoryListAndChart(true) // true for income
        }

        expenseRadioButton.setOnClickListener {
            updateCategoryListAndChart(false) // false for outcome
        }

        backTxt.setOnClickListener {
            val intent = Intent(this, HomeAct::class.java)
            startActivity(intent)
        }


    }

    private fun updateCategoryListAndChart(isIncome: Boolean) {
        val statDao = StatDao(this)

        val selectedPeriod = periodSpinner.selectedItem.toString() // Get selected period
        val categoryTotals = statDao.getStatByCategory(selectedPeriod, isIncome) // Retrieve category totals from StatDao

        // Clear existing items in catList
        catList.removeAllViews()

        // Store data for PieChart
        val chartEntries = ArrayList<PieEntry>()

        // Loop through the results from getStatByCategory
        for ((categoryName, totalMoney) in categoryTotals) {
            // Update the UI for each category item
            val catView = layoutInflater.inflate(R.layout.cat_item, catList, false)
            val categoryNameView = catView.findViewById<TextView>(R.id.IdTextView)
            val totalMoneyView = catView.findViewById<TextView>(R.id.NameTextView)
            val menuOptions = catView.findViewById<ImageView>(R.id.menuOptions)

            categoryNameView.text = categoryName
            totalMoneyView.text = totalMoney.toString()

            // Set up the menuOptions click listener to display the popup menu
            menuOptions.setOnClickListener {
                displayMenu(it, categoryName, selectedPeriod)
            }

            catList.addView(catView)

            // Add data to the PieChart
            chartEntries.add(PieEntry(totalMoney.toFloat(), categoryName))
        }

        // Update PieChart with the new data
        updatePieChart(chartEntries)
    }

    private fun updatePieChart(entries: List<PieEntry>) {
        val pieDataSet: PieDataSet

        if (entries.isEmpty()) {
            // Add a default entry with a neutral value for empty data
            val defaultEntry = PieEntry(1f, "")
            pieDataSet = PieDataSet(listOf(defaultEntry), "")

            // Set the color to gray for the default entry
            pieDataSet.colors = listOf(Color.GRAY)

            // Display a message in the center
            pieChart.centerText = "No category data available"
            pieChart.setCenterTextSize(12f)
            pieChart.setCenterTextColor(Color.GRAY)
        } else {
            // Normal setup for pie chart when data is available
            pieDataSet = PieDataSet(entries, "")

            // Set unique colors for each category
            val colors = listOf(
                Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.LTGRAY
            )
            pieDataSet.colors = colors

            // Customize text size for values inside the pie slices
            pieDataSet.valueTextSize = 8f
            pieDataSet.valueTextColor = Color.BLACK

            // Format the data labels to show them clearly
            pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.valueLinePart1Length = 0.3f
            pieDataSet.valueLinePart2Length = 0.3f
            pieDataSet.valueLineColor = Color.DKGRAY

            // Remove the center text if previously set
            pieChart.centerText = ""
        }

        // Customize PieChart properties
        val data = PieData(pieDataSet)
        data.setValueFormatter(PercentFormatter(pieChart)) // Optional, to show percentages if desired

        pieChart.data = data
        pieChart.setEntryLabelTextSize(8f)
        pieChart.setEntryLabelColor(Color.BLACK)

        // Customize the legend to position it below the description
        val legend = pieChart.legend
        legend.isEnabled = true
        legend.form = Legend.LegendForm.SQUARE
        legend.formSize = 10f
        legend.textSize = 14f
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.yOffset = 20f // Adjust spacing between legend and description

        // Refresh the chart
        pieChart.invalidate()
    }



    private fun displayMenu(view: View, categoryName: String, period: String) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.cat_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.cat_detail -> {
                    // Start CatDetailAct and pass necessary data
                    val intent = Intent(this, CatDetailAct::class.java)
                    intent.putExtra("categoryName", categoryName)
                    intent.putExtra("period", period)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}