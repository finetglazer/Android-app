package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.StatDao
import java.text.SimpleDateFormat
import java.util.Locale

class CatDetailAct :Activity() {

    private lateinit var backTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_transaction_detail)

        backTxt = findViewById(R.id.back)
        backTxt.setOnClickListener {
            val intent = Intent(this, StatCatAct::class.java)
            startActivity(intent)

        }

        // Retrieve passed data
        val categoryName = intent.getStringExtra("categoryName") ?: ""
        val period = intent.getStringExtra("period") ?: ""

        // Set category name in UI
        findViewById<TextView>(R.id.txtNameCat).text = categoryName

        // Fetch and display transactions
        loadTransactions(categoryName, period)
    }

    private fun loadTransactions(categoryName: String, period: String) {
        val transactions = StatDao(this).getTransactionsByCategoryAndPeriod(categoryName, period)

        val transactionList = findViewById<LinearLayout>(R.id.transaction_list)
        transactionList.removeAllViews()

        for (transaction in transactions) {
            val transactionView = layoutInflater.inflate(R.layout.tran_simple_item, transactionList, false)
            val dateView = transactionView.findViewById<TextView>(R.id.date)
            val amountView = transactionView.findViewById<TextView>(R.id.amount)


            val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = transaction.date?.let { displayDateFormat.format(it) } ?: "N/A"
            dateView.text = formattedDate


            amountView.text = transaction.amount.toString()

            transactionList.addView(transactionView)
        }
    }
}