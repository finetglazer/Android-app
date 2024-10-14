package com.map.hung.myapplication.dao

import android.content.ContentValues
import android.content.Context
import com.map.hung.myapplication.model.CatInOut
import com.map.hung.myapplication.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionDao(context: Context) {

    private val dbHelper: DbHelper = DbHelper(context)

    fun add(transaction: Transaction): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", transaction.name)
            put("amount", transaction.amount)

            val dateformatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = dateformatter.format(transaction.date)

            put("date", dateString)
            put("note", transaction.note)
            put("idCateInOut", transaction.catInOut?.id)
        }

        val result = db.insert("tblTransaction", null, values)
        return result != -1L
    }

    fun edit(transaction: Transaction, newTransaction: Transaction): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", transaction.name)
            put("amount", transaction.amount)
            put("note", transaction.note)
            put("catInOut", transaction.catInOut?.id)


            val dateformatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = dateformatter.format(newTransaction.date)

            put("date", dateString)
        }



        val result = db.update(
            "tblTransaction",
            values,
            "id = ?",
            arrayOf(transaction.id.toString())
        )

        return result != 0
    }

    fun delete(transaction: Transaction): Boolean {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            "tblTransaction",
            "id = ?",
            arrayOf(transaction.id.toString())
        )

        return result != 0
    }

    // search by date
    fun search(day: Date) : List<Transaction> {
        val dateformatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = dateformatter.format(day)

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM tblTransaction WHERE date = ?",
            arrayOf(dateString)
        )

        val transactions = mutableListOf<Transaction>()

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex("id")
            val nameIndex = cursor.getColumnIndex("name")
            val amountIndex = cursor.getColumnIndex("amount")
            val dateIndex = cursor.getColumnIndex("date")
            val noteIndex = cursor.getColumnIndex("note")
            val catInOutIndex = cursor.getColumnIndex("idCateInOut")

            val transaction = Transaction(
                id = if (idIndex != -1) cursor.getInt(idIndex) else 0,  // Default value or handle differently
                name = if (nameIndex != -1) cursor.getString(nameIndex) else "",  // Default value or handle differently
                catInOut = if (catInOutIndex != -1) CatInOut(cursor.getInt(catInOutIndex)) else null,  // Default value or handle differently
                amount = if (amountIndex != -1) cursor.getDouble(amountIndex) else 0.0,  // Default value or handle differently
                date = if (dateIndex != -1) dateformatter.parse(cursor.getString(dateIndex)) else Date(),  // Default value or handle differently
                note = if (noteIndex != -1) cursor.getString(noteIndex) else ""  // Default value or handle differently

            )
            transactions.add(transaction)
        }

        cursor.close()
        return transactions


    }

}