package com.map.hung.myapplication.dao

import android.content.Context
import android.util.Log
import com.map.hung.myapplication.model.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatDao(context: Context) {

    private val dbHelper: DbHelper = DbHelper(context)

    fun getStatByCategory(period: String, isIncome: Boolean): Map<String, Double> {
        val db = dbHelper.readableDatabase
        val inOutType = if (isIncome) 1 else 2
        val query = """
            SELECT T.name, SUM(T.amount)
            FROM tblTransaction T 
            JOIN tblCatInOut TCIO ON TCIO.id = T.idCateInOut
            WHERE TCIO.idInOut = $inOutType AND ${getDateCondition(period)}
            GROUP BY T.name
        """

        val cursor = db.rawQuery(query, null)
        val statMap = mutableMapOf<String, Double>()
        while (cursor.moveToNext()) {
            statMap[cursor.getString(0)] = cursor.getDouble(1)
        }
        cursor.close()
        return statMap
    }

    fun getTransactionsByCategoryAndPeriod(categoryName: String, period: String): List<Transaction> {
        val db = dbHelper.readableDatabase

        val query = """
            SELECT T.date, T.amount
            FROM tblTransaction T
            WHERE T.name = ? AND ${getDateCondition(period)}
        """

        val cursor = db.rawQuery(query, arrayOf(categoryName))
        val transactions = mutableListOf<Transaction>()

        val dbDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        while (cursor.moveToNext()) {
            val dateIndex = cursor.getColumnIndex("date")
            val amountIndex = cursor.getColumnIndex("amount")

            if (dateIndex != -1 && amountIndex != -1) {
                val amount = cursor.getDouble(amountIndex)
                val dateString = cursor.getString(dateIndex)

                val date = dbDateFormat.parse(dateString)
                transactions.add(Transaction(name = categoryName, date = date, amount = amount))
            } else {
                Log.e("DatabaseError", "Column not found in query results.")
            }
        }

        cursor.close()
        return transactions
    }

    private fun getDateCondition(period: String): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate: String
        val endDate: String

        when (period) {
            "Tháng hiện tại" -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                startDate = sdf.format(cal.time)
                cal.add(Calendar.MONTH, 1)
                cal.add(Calendar.DAY_OF_MONTH, -1)
                endDate = sdf.format(cal.time)
            }
            "Tháng trước" -> {
                cal.add(Calendar.MONTH, -1)
                cal.set(Calendar.DAY_OF_MONTH, 1)
                startDate = sdf.format(cal.time)
                cal.add(Calendar.MONTH, 1)
                cal.add(Calendar.DAY_OF_MONTH, -1)
                endDate = sdf.format(cal.time)
            }
            "Năm hiện tại" -> {
                cal.set(Calendar.DAY_OF_YEAR, 1)
                startDate = sdf.format(cal.time)
                cal.add(Calendar.YEAR, 1)
                cal.add(Calendar.DAY_OF_YEAR, -1)
                endDate = sdf.format(cal.time)
            }
            "Năm ngoái" -> {
                cal.add(Calendar.YEAR, -1)
                cal.set(Calendar.DAY_OF_YEAR, 1)
                startDate = sdf.format(cal.time)
                cal.add(Calendar.YEAR, 1)
                cal.add(Calendar.DAY_OF_YEAR, -1)
                endDate = sdf.format(cal.time)
            }
            else -> return "1=1"
        }

        return "strftime('%Y-%m-%d', substr(T.date, 7, 4) || '-' || substr(T.date, 4, 2) || '-' || substr(T.date, 1, 2)) BETWEEN '$startDate' AND '$endDate'"
    }

    fun getMonthlyStatistics(): List<Triple<String, Double, Double>> {
        val results = mutableListOf<Triple<String, Double, Double>>()
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        for (month in 1..12) {
            val monthStart = getMonthStart(currentYear, month)
            val monthEnd = getMonthEnd(currentYear, month)

            val income = getTotalByDateRange(monthStart, monthEnd, true)
            val outcome = getTotalByDateRange(monthStart, monthEnd, false)

            // Add each month with both income and outcome
            results.add(Triple("T$month", income, outcome))
        }
        return results
    }

    fun getYearlyStatistics(): List<Triple<String, Double, Double>> {
        val results = mutableListOf<Triple<String, Double, Double>>()
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        for (year in currentYear - 4..currentYear) {
            val yearStart = "$year-01-01"
            val yearEnd = "$year-12-31"

            val income = getTotalByDateRange(yearStart, yearEnd, true)
            val outcome = getTotalByDateRange(yearStart, yearEnd, false)

            // Add each year with both income and outcome
            results.add(Triple(year.toString(), income, outcome))
        }
        return results
    }

    private fun getTotalByDateRange(startDate: String, endDate: String, isIncome: Boolean): Double {
        val db = dbHelper.readableDatabase
        val inOutType = if (isIncome) 1 else 2
        val query = """
        SELECT SUM(t.amount) 
        FROM tblTransaction t
        JOIN tblCatInOut tcio ON tcio.id = t.idCateInOut
        WHERE tcio.idInOut = $inOutType AND 
            strftime('%Y-%m-%d', substr(t.date, 7, 4) || '-' || substr(t.date, 4, 2) || '-' || substr(t.date, 1, 2))
            BETWEEN '$startDate' AND '$endDate'
    """
        val cursor = db.rawQuery(query, null)
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }

    private fun getMonthStart(year: Int, month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Standard format for querying
        return dateFormat.format(calendar.time)
    }

    private fun getMonthEnd(year: Int, month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Standard format for querying
        return dateFormat.format(calendar.time)
    }

}
