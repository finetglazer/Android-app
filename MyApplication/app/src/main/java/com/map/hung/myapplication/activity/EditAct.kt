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
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.UserDao
import com.map.hung.myapplication.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditAct : Activity() {
    private lateinit var editUserName: EditText
    private lateinit var editPassWord: EditText
    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var btnReset: Button
    private lateinit var btnBack: Button
    private var spinner: Spinner? = null
    private var editTextDatePicker: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit) // Assuming the XML layout file is named edit.xml

        val userDao = UserDao(this)

        editUserName = findViewById(R.id.edit_username)
        editPassWord = findViewById(R.id.edit_password)
        editFullName = findViewById(R.id.edit_fullname)
        editEmail = findViewById(R.id.edit_email)
        editPhone = findViewById(R.id.edit_phone)
        btnSave = findViewById(R.id.btn_Save)
        btnReset = findViewById(R.id.btn_Reset)
        btnBack = findViewById(R.id.btn_back)
        spinner = findViewById(R.id.spinner)
        editTextDatePicker = findViewById(R.id.editText_datepicker)

        // Retrieve user from SharedPreferences
        val user: User? = intent.getParcelableExtra("user")

        if (user != null) {
            editUserName.setText(user.username)
            editPassWord.setText(user.password)
            editFullName.setText(user.fullname)
            editEmail.setText(user.email)
            editPhone.setText(user.phone)
//            spinner!!.setSelection(0)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDob = user.dob?.let { dateFormat.format(it) } ?: "No Date Available"
            editTextDatePicker?.setText(formattedDob)

        }

        // prepare data
        val items = listOf("male", "female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //set adapter to spinner

        spinner!!.adapter = adapter

        var check = 0
        //handle selection event
        spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if (check == 0) {
                    check = 1
                    return
                }
                Toast.makeText(applicationContext, "Selected : $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        })
        //set default value for spinner
        val gender = user?.gender
        if (gender?.equals("male") == true) {
            spinner!!.setSelection(0)
        } else {
            spinner!!.setSelection(1)
        }

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
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    // Format the selected date
                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    // Update the EditText with the selected date
                    editTextDatePicker!!.setText(selectedDate)
                },
                year, month, day
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }


        // Save changes
        btnSave!!.setOnClickListener {
            // check any empty field
            if (editUserName!!.text.isEmpty() || editPassWord!!.text.isEmpty() || editFullName!!.text.isEmpty() || editEmail!!.text.isEmpty() || editTextDatePicker!!.text.isEmpty() || editPhone!!.text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val username = editUserName!!.text.toString()
            val password = editPassWord!!.text.toString()
            val fullname = editFullName!!.text.toString()
            val email = editEmail!!.text.toString()
            val gender = spinner!!.selectedItem.toString()
            val phone = editPhone!!.text.toString()


            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dobString = editTextDatePicker!!.text.toString()
            val dob = dateFormat.parse(dobString)

            val newUser = User(username, password, fullname, email, dob, gender, phone)

            //if it exist, show toast message
            if (userDao.userExists(username, email) && (username != user?.username || email != user?.email)) {
                if (userDao.userUserNameExists(username) && username != user?.username) {
                    editUserName!!.error = "Username already exists"
                    return@setOnClickListener
                }
                if (userDao.userEmailExists(email) && email != user?.email) {
                    editEmail!!.error = "Email already exists"
                    return@setOnClickListener
                }

            }


            // If no match is found, save the new user
            val userId = userDao.updateUser(user!!, newUser)

            if (userId > 0) {
                //show toasted message
                Toast.makeText(this, "User is edited successfully", Toast.LENGTH_SHORT).show()
                editUserName!!.setText("")
                editPassWord!!.setText("")
                editFullName!!.setText("")
                editEmail!!.setText("")
                editPhone!!.setText("")

                // back to search view
                val intent = Intent(this, SearchAct::class.java)
                startActivity(intent)
            }

        }

        btnReset.setOnClickListener {
            editUserName.setText(user?.username)
            editPassWord.setText(user?.password)
            editFullName.setText(user?.fullname)
            editEmail.setText(user?.email)
            editPhone.setText(user?.phone)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDob = user?.dob?.let { dateFormat.format(it) } ?: "No Date Available"
            editTextDatePicker?.setText(formattedDob)
            val gender = user?.gender
            if (gender?.equals("male") == true) {
                spinner!!.setSelection(0)
            } else {
                spinner!!.setSelection(1)
            }
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, SearchAct::class.java)
            startActivity(intent)

        }
    }

}
