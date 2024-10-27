package com.map.hung.myapplication.dao

import android.content.ContentValues
import android.content.Context
import com.map.hung.myapplication.model.DailyStat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyStatDao(context: Context) {
    private val dbHelper: DbHelper = DbHelper(context)

    // Add a DailyStat to the database
    fun add(dailyStat: DailyStat): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // Convert Date to a formatted string, if storing as text
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            put("day", dateFormatter.format(dailyStat.day))
            put("income", dailyStat.income)
            put("outcome", dailyStat.outcome)
        }

        val result = db.insert("tblDailyStat", null, values)
        db.close()
        return result != -1L
    }

    // Retrieve a DailyStat by day
    fun getDayStat(day: Date): DailyStat? {
        // Convert the Date to the same string format used in the database
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateFormatter.format(day)

        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tblDailyStat",
            null,
            "day = ?",  // Use the correct format for querying day
            arrayOf(dateString),
            null,
            null,
            null
        )

        val dailyStat: DailyStat? = if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id")) // Use getInt for ID
            val income = cursor.getInt(cursor.getColumnIndexOrThrow("income")) // Retrieve income
            val outcome = cursor.getInt(cursor.getColumnIndexOrThrow("outcome")) // Retrieve outcome

            // Create a DailyStat object and return it
            DailyStat(id, day, income, outcome)
        } else {
            null
        }

        cursor.close() // Close the cursor to avoid memory leaks
        db.close() // Close the database after the operation
        return dailyStat
    }

    // get by month return a list of DailyStat
    fun getMonthStat(mont: Date): List<DailyStat> {

        val dateFormatter = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val dateString = dateFormatter.format(mont)

        // get month and year

        val month = dateString.substring(0, 2).toInt()
        val year = dateString.substring(3).toInt()

        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tblDailyStat",
            null,
            "strftime('%m', day) = ? AND strftime('%Y', day) = ?", // Query by month and year
            arrayOf(month.toString(), year.toString()),
            null,
            null,
            null
        )

        val dailyStats = mutableListOf<DailyStat>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val day = Date(cursor.getLong(cursor.getColumnIndexOrThrow("day")))
            val income = cursor.getInt(cursor.getColumnIndexOrThrow("income"))
            val outcome = cursor.getInt(cursor.getColumnIndexOrThrow("outcome"))

            dailyStats.add(DailyStat(id, day, income, outcome))
        }

        cursor.close()
        db.close()
        return dailyStats
    }
}
// Compare this snippet from app/src/main/java/com/map/hung/myapplication/dao/TransactionDao.kt:
