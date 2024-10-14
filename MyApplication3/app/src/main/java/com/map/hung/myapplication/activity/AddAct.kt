package com.map.hung.myapplication.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.CatInOutDao
import com.map.hung.myapplication.dao.CategoryDao
import com.map.hung.myapplication.dao.TransactionDao
import com.map.hung.myapplication.model.CatInOut
import com.map.hung.myapplication.model.InOut
import com.map.hung.myapplication.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

class AddAct: Activity() {
    private lateinit var incomeBtn: RadioButton
    private lateinit var expenseBtn: RadioButton
    private lateinit var addButton: Button
    private lateinit var titleSpinner: Spinner
    private lateinit var noteText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var editDate: EditText
    private lateinit var backBtn: TextView
    private lateinit var menu: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add)
        val categoryDao = CategoryDao(this)

        incomeBtn = findViewById(R.id.income)
        expenseBtn = findViewById(R.id.expense)
        titleSpinner = findViewById(R.id.spinnerItem)
        addButton = findViewById(R.id.BtnAdd)
        noteText = findViewById(R.id.noteEdit)
        editDate = findViewById(R.id.editText_datepicker)
        amountEditText = findViewById(R.id.amountEdit)
        backBtn = findViewById(R.id.backBtn)
        menu = findViewById(R.id.menuOptions)

        var check = 1
        incomeBtnOnClick()

        incomeBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                check = 1
                incomeBtnOnClick()
            }
        }

        expenseBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                check = 2
                expenseBtnOnClick()
            }
        }

        //set up menu Options
        menu.setOnClickListener {
            displayMenu(menu);
        }

        // Set up calendar icon
        val iconSize = 50
        val calendarIcon = ContextCompat.getDrawable(this, R.drawable.calendar)
        calendarIcon!!.setBounds(0, 0, iconSize, iconSize)
        editDate.setCompoundDrawables(null, null, calendarIcon, null)

        setCurrentDate()

        editDate.setOnClickListener {
            editDateOnClick()
        }

        addButton.setOnClickListener {
            // Call addOnClick when the button is clicked
            addOnClick(check)
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, HomeAct::class.java)
            startActivity(intent)
        }
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDate = "$day/${month + 1}/$year"
        editDate.setText(currentDate)
    }

    private fun incomeBtnOnClick() {
        val categoryDao = CategoryDao(this)
        val items = categoryDao.searchByInOut(true)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        titleSpinner.adapter = adapter
    }

    private fun expenseBtnOnClick() {
        val categoryDao = CategoryDao(this)
        val items = categoryDao.searchByInOut(false)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        titleSpinner.adapter = adapter
    }

    private fun editDateOnClick() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                editDate.setText(selectedDate)
            },
            year, month, day)

        datePickerDialog.show()
    }

    private fun addOnClick(check: Int) {
        // Fetch the latest data when "Add" button is clicked
        val categoryDao = CategoryDao(this)
        val catInOutDao = CatInOutDao(this)

        val name = titleSpinner.selectedItem.toString()
        val amountText = amountEditText.text.toString()

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = editDate.text.toString()
        val date = dateFormat.parse(dateString)
        val note = noteText.text.toString()

        // Check the radio button to determine income or expense

        // Fetch the category based on the spinner selection
        val category = categoryDao.searchByName(name)

        val catInOut = catInOutDao.findIdCatInOut(category?.id!!, check)

        // Create the transaction object here with the latest values
        val transaction = Transaction(
            name = name,
            catInOut = catInOut,
            amount = amount,
            date = date,
            note = note,
        )

        // Add the transaction to the database
        val transactionDao = TransactionDao(this)
        transactionDao.add(transaction)

        // Show confirmation to the user
        Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show()

        // Optionally, clear the form fields after adding the transaction
        clearForm()
    }

    private fun clearForm() {
        amountEditText.text.clear()
        noteText.text.clear()
        setCurrentDate() // Reset the date to the current date
        titleSpinner.setSelection(0) // Reset spinner to the first item
    }

    fun displayMenu(view: View) {
        // Create the popup menu, anchored to the view (e.g., a button)
        val popupMenu = PopupMenu(this, view)

        // Inflate the popup menu from the XML resource
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_option -> {
                    // Start a new activity when the "Edit" option is clicked
                    val intent = Intent(this, AddCatAct::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()
    }

}
