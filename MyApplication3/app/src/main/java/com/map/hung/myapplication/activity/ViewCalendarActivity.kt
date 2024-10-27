package com.map.hung.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout
import com.map.hung.myapplication.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.core.content.res.ResourcesCompat
import com.map.hung.myapplication.dao.CatInOutDao

import com.map.hung.myapplication.dao.TransactionDao
import java.util.Date
import android.graphics.Color

class ViewCalendarActivity : AppCompatActivity() {

    private lateinit var gridLayoutCalendar: GridLayout
    private lateinit var textViewCurrentMonth: TextView
    private lateinit var buttonPreviousMonth: Button
    private lateinit var buttonNextMonth: Button
    private lateinit var backBtn: TextView


    private var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_calendar)




        // Khởi tạo các view
        gridLayoutCalendar = findViewById(R.id.gridLayout_calendar)
        textViewCurrentMonth = findViewById(R.id.current_month)
        buttonPreviousMonth = findViewById(R.id.button_previous_month)
        buttonNextMonth = findViewById(R.id.button_next_month)



        // Hiển thị tháng hiện tại
        updateMonthDisplay()

        // Nút quay lại MainActivity
        backBtn = findViewById<Button>(R.id.back_Btn)
        backBtn.setOnClickListener {
            val intent = Intent(this, HomeAct::class.java)
            startActivity(intent)
        }

        // Xử lý nút Previous và Next
        buttonPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateMonthDisplay()
        }

        buttonNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateMonthDisplay()
        }

        // Hiển thị lịch
        populateCalendar()
    }

    private fun updateMonthDisplay() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        textViewCurrentMonth.text = dateFormat.format(calendar.time)
        populateCalendar()
    }

    private fun populateCalendar() {
        gridLayoutCalendar.removeAllViews()

        // Hiển thị các thứ trong tuần (từ thứ Hai đến Chủ nhật)
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        for (dayOfWeek in daysOfWeek) {
            val dayOfWeekView = TextView(this)
            dayOfWeekView.text = dayOfWeek
            dayOfWeekView.textSize = 9.5f
            dayOfWeekView.gravity = Gravity.CENTER
            dayOfWeekView.setPadding(16, 16, 16, 16)
            dayOfWeekView.setTypeface(ResourcesCompat.getFont(this, R.font.comici))
            dayOfWeekView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            gridLayoutCalendar.addView(dayOfWeekView)
        }

        // Đặt calendar tới ngày 1 của tháng hiện tại
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // Lấy thứ của ngày 1 trong tuần (Chủ Nhật là 1, Thứ Hai là 2, ...)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
        val offset = if (firstDayOfMonth == Calendar.SUNDAY) 6 else firstDayOfMonth - 2

        // Điền các ô trống trước ngày 1 (nếu ngày 1 không phải là Thứ Hai)
        for (i in 0 until offset) {
            val emptyView = TextView(this)
            emptyView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            gridLayoutCalendar.addView(emptyView)
        }

        // Điền các ngày trong tháng
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..daysInMonth) {
            val dayLayout = LinearLayout(this)
            dayLayout.orientation = LinearLayout.VERTICAL
            dayLayout.gravity = Gravity.CENTER

            // Hiển thị số ngày
            val dayView = TextView(this)
            dayView.text = day.toString()
            dayView.textSize = 16f
            dayView.gravity = Gravity.CENTER
            dayView.setPadding(16, 16, 16, 16)
            dayLayout.addView(dayView)

            // Kiểm tra số lần thu và chi cho ngày này
            val transactionCount = getTransactionCountForDay(day)
            val (incomeCount, expenseCount) = transactionCount

// Display income dot and count if income exists
            if (incomeCount > 0.0) {
                // if incomeCount is thousands, we will format it to k, million to m, billion to b
                val incomeCountString = if (incomeCount >= 1000000000) {
                    "${(incomeCount / 1000000000).toInt()}b"
                } else if (incomeCount >= 1000000) {
                    "${(incomeCount / 1000000).toInt()}m"
                } else if (incomeCount >= 1000) {
                    "${(incomeCount / 1000).toInt()}k"
                } else {
                    incomeCount.toString()
                }

                // Create a horizontal LinearLayout to hold the dot and count
                val incomeLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 4
                    }
                }

                // Create the income dot
                val incomeDot = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(20, 20)
                    setBackgroundResource(R.drawable.green)  // Green dot for income
                }
                incomeLayout.addView(incomeDot)

                // Create the income count TextView
                val incomeTextView = TextView(this).apply {
                    text = incomeCountString
                    setTextColor(Color.GREEN)       // Text color matching the dot
                    textSize = 12f                  // Adjust text size as needed
                    setPadding(8, 0, 0, 0)          // Optional: Add padding between dot and text
                }
                incomeLayout.addView(incomeTextView)

                // Add the income layout to your dayLayout
                dayLayout.addView(incomeLayout)
            }

// Display expense dot and count if expense exists
            if (expenseCount > 0.0) {
                // outcomeCount is formatted to k, m, b
                val outcomeCountString = if (expenseCount >= 1000000000) {
                    "${(expenseCount / 1000000000).toInt()}b"
                } else if (expenseCount >= 1000000) {
                    "${(expenseCount / 1000000).toInt()}m"
                } else if (expenseCount >= 1000) {
                    "${(expenseCount / 1000).toInt()}k"
                } else {
                    expenseCount.toString()
                }
                // Create a horizontal LinearLayout to hold the dot and count
                val expenseLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 4
                    }
                }

                // Create the expense dot
                val expenseDot = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(20, 20)
                    setBackgroundResource(R.drawable.red)  // Red dot for expense
                }
                expenseLayout.addView(expenseDot)

                // Create the expense count TextView
                val expenseTextView = TextView(this).apply {
                    text = outcomeCountString
                    setTextColor(Color.RED)         // Text color matching the dot

                    textSize = 12f                  // Adjust text size as needed
                    setPadding(8, 0, 0, 0)          // Optional: Add padding between dot and text
                }
                expenseLayout.addView(expenseTextView)

                // Add the expense layout to your dayLayout
                dayLayout.addView(expenseLayout)
            }

            // Đặt layout params cho mỗi ô ngày trong GridLayout
            dayLayout.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }

            val selectedDate = "$day/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"

            // Thêm sự kiện click vào mỗi ngày
            dayLayout.setOnClickListener {
                val intent = Intent(this, DayTransactionsActivity::class.java)
                intent.putExtra("selected_date", selectedDate) // Pass the selected date to the new activity
                startActivity(intent)
            }


            // Thêm dayLayout vào gridLayoutCalendar
            gridLayoutCalendar.addView(dayLayout)
        }

        // Sau khi điền hết số ngày trong tháng, thêm các ô trống nếu cần để đảm bảo 6 hàng đầy đủ
        val totalCells = offset + daysInMonth
        val cellsNeeded = 42 - totalCells  // Cần đủ 42 ô cho 6 hàng, 7 cột
        for (i in 0 until cellsNeeded) {
            val emptyView = TextView(this)
            emptyView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            gridLayoutCalendar.addView(emptyView)
        }
    }

    private fun getTransactionCountForDay(day: Int): Pair<Double, Double> {
        val transactionDao = TransactionDao(this)
        val catInOutDao = CatInOutDao(this)

        // Lấy tháng và năm hiện tại từ calendar
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val selectedDate = "$day/$currentMonth/$currentYear"  // Tạo ngày được chọn theo định dạng dd/MM/yyyy
        // change selectedDate into date type

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date = dateFormat.parse(selectedDate) ?: throw IllegalArgumentException("Invalid date format")

        val transactions = transactionDao.search(date)
        Log.d("hung", "transactions: $transactions")


        var incomeCount = 0.0
        var expenseCount = 0.0
        for (transaction in transactions) {
            val catInOut = transaction.catInOut
            val idInOut = catInOut?.id?.let { catInOutDao.findIdInOut(it) }

            val amount : Double? = transaction.amount
            if (idInOut == 1) {
                if (amount != null) {
                    incomeCount += amount
                }
            } else if (idInOut == 2) {
                if (amount != null) {
                    expenseCount += amount
                }
            }

        }
        return Pair(incomeCount, expenseCount)
    }
}