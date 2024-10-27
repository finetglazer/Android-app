package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.runtime.currentCompositionErrors
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.CatInOutDao
import com.map.hung.myapplication.dao.TransactionDao
import com.map.hung.myapplication.model.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeAct : Activity(){

    private lateinit var scrollView: ScrollView
    private lateinit var txt_back: TextView
    private lateinit var addButton: Button
    private lateinit var transactionList: LinearLayout
    private lateinit var incomeTextView: TextView
    private lateinit var expenseTextView: TextView
    private lateinit var txtDetail: TextView
    private lateinit var menuImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search) // Assuming the XML layout file is named home.xml
        val transactionDao = TransactionDao(this)

        scrollView = findViewById(R.id.scrollView)
        transactionList = findViewById(R.id.transaction_list)
        addButton = findViewById(R.id.add_button)
        incomeTextView = findViewById(R.id.income_amount)
        expenseTextView = findViewById(R.id.expense_amount)
        txtDetail = findViewById(R.id.id_detail)
        menuImage = findViewById(R.id.menu)

        txtDetail.setOnClickListener {
            val intent = Intent(this, ViewCalendarActivity::class.java)
            startActivity(intent)
        }


        addButton.setOnClickListener {
            val intent = Intent(this, AddAct::class.java)
            startActivity(intent)
        }

        // Showing the data of the current day
        val currentDate = Calendar.getInstance().time
        val transactions = transactionDao.search(currentDate) // Pass current date to search()

        // Display transactions in transactionList
        displayTransactions(transactionList ,transactions)

        var income = 0.0
        var expense = 0.0

        transactions.forEach { transaction ->
            if (transaction.catInOut?.id!! >= 1 && transaction.catInOut!!.id!! <=5) {
                income += transaction.amount!!
            } else {
                expense += transaction.amount!!
            }
        }

        incomeTextView.text = "Thu: $income"
        expenseTextView.text = "Chi: $expense"

        menuImage.setOnClickListener{
            display(menuImage)
        }


    }

    private fun displayTransactions(container: LinearLayout, transactions: List<Transaction>) {
        container.removeAllViews()
        container.visibility = View.VISIBLE

        transactions.forEach { transaction ->
            val transactionView = layoutInflater.inflate(R.layout.transaction_item, container, false)

            transactionView.findViewById<TextView>(R.id.IdTextView).text = "ID: ${transaction.id}"
            transactionView.findViewById<TextView>(R.id.NameTextView).text = "Name: ${transaction.name}"
            transactionView.findViewById<TextView>(R.id.amountTextView).text = "Amount: ${transaction.amount}"
            transactionView.findViewById<TextView>(R.id.dateTextView).text = "Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(transaction.date)}"
            transactionView.findViewById<TextView>(R.id.noteTextView).text = "Note: ${transaction.note}"

            // check if transaction.catInOut.inOut is 1 or 2
            // 1 -> Thu
            // 2 -> Chi

            val catInOutDao = CatInOutDao(this)
            val idInOUt = catInOutDao.findIdInOut(transaction.catInOut?.id!!)

            if (idInOUt == 1) {
                transactionView.findViewById<TextView>(R.id.CateInOutTextView).text = "Thu/Chi: Thu"
            } else {
                transactionView.findViewById<TextView>(R.id.CateInOutTextView).text = "Thu/Chi: Chi"
            }

            container.addView(transactionView)
        }
    }

    private fun display(view: View) {
        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.stat_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.stat_cat -> {
                    val intent = Intent(this, StatCatAct::class.java)
                    startActivity(intent)
                    true
                }
                R.id.stat_time -> {
                    val intent = Intent(this, StatTimeAct::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()

    }

}