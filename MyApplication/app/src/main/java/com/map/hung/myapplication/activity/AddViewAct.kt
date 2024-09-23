package com.map.hung.myapplication.activity


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddViewAct : Activity() {
    private var txtUsername: EditText? = null
    private var  txtPassword: EditText? = null
    private var txtFullname: EditText? = null
    private var txtEmail: EditText? = null
    private var btnAdd: Button? = null
    private var btnBack: TextView? = null
    private var spinner: Spinner? = null
    private var editTextDatePicker: EditText? = null

    // Add onCreate
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add)

        txtUsername = findViewById(R.id.usernameEdit)
        txtPassword = findViewById(R.id.password)
        txtFullname = findViewById(R.id.fullname)
        txtEmail = findViewById(R.id.email)
        btnAdd = findViewById(R.id.add)
        btnBack = findViewById(R.id.back)
        spinner = findViewById(R.id.gender)
        editTextDatePicker = findViewById(R.id.editText_datepicker)

        // prepare data
        val items = listOf("male", "female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //set adapter to spinner

        spinner!!.adapter = adapter

        var check = 0
        //handle selection event
        spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if (check == 0) {
                    check = 1
                    return
                }
                Toast.makeText(applicationContext, "Selected : $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        })

        val calendarIcon = ContextCompat.getDrawable(this, R.drawable.calendar)

        val iconSize = 50
        calendarIcon?.setBounds(0, 0, iconSize, iconSize)
        editTextDatePicker!!.setCompoundDrawables(null, null, calendarIcon, null)
        // set up date picker
        editTextDatePicker!!.setOnClickListener {
            // Get the current date (year, month, day)
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Open DatePickerDialog
            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    // Format the selected date
                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    // Update the EditText with the selected date
                    editTextDatePicker!!.setText(selectedDate)
                },
                year, month, day)

            // Show the DatePickerDialog
            datePickerDialog.show()
        }

        btnAdd!!.setOnClickListener {
            // check any empty field
            if (txtUsername!!.text.isEmpty() || txtPassword!!.text.isEmpty() || txtFullname!!.text.isEmpty() || txtEmail!!.text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val username = txtUsername!!.text.toString()
            val password = txtPassword!!.text.toString()
            val fullname = txtFullname!!.text.toString()
            val email = txtEmail!!.text.toString()
            val gender = spinner!!.selectedItem.toString()


            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dobString = editTextDatePicker!!.text.toString()
            val dob = dateFormat.parse(dobString)

            val user = User(username, password, fullname, email, dob, gender)
            // add gender and date of birth


            // Retrieve the list of saved users
            val userPreferences = UserPreferences(this)
            val savedUsers = userPreferences.getUsers() ?: arrayOf()
            //if it exist, show toast message
            // Check if the username or email already exists
            var userExists = false
            for (savedUser in savedUsers) {
                if (savedUser.username == username) {
                    userExists = true
                    txtUsername!!.error = "Username already exists"
                    break
                }
                if (savedUser.email == email) {
                    userExists = true
                    txtEmail!!.error = "Email already exists"
                    break
                }
            }

            // If no match is found, save the new user
            if (!userExists) {
                val newUsersList: Array<User> = savedUsers.plus(user) // Add the new user to the list
                userPreferences.addUser(newUsersList)  // Save the updated list
                //show toasted message
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                txtUsername!!.setText("")
                txtPassword!!.setText("")
                txtFullname!!.setText("")
                txtEmail!!.setText("")
                editTextDatePicker!!.setText("")
                spinner!!.setSelection(0)
            }
        }

        btnBack!!.setOnClickListener {
            val intent = Intent(this, UserHomeAct::class.java)
            startActivity(intent)
        }
    }
}


