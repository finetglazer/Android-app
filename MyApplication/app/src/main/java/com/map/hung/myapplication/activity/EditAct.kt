package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User

class EditAct : Activity() {
    private lateinit var editUserName: EditText
    private lateinit var editPassWord: EditText
    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var btnReset: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit) // Assuming the XML layout file is named edit.xml

        editUserName = findViewById(R.id.edit_username)
        editPassWord = findViewById(R.id.edit_password)
        editFullName = findViewById(R.id.edit_fullname)
        editEmail = findViewById(R.id.edit_email)
        btnSave = findViewById(R.id.btn_Save)
        btnReset = findViewById(R.id.btn_Reset)
        btnBack = findViewById(R.id.btn_back)

        // Retrieve user from SharedPreferences
        val user: User? = intent.getParcelableExtra("user")
        val userPreferences = UserPreferences(this)
        val savedUsers = userPreferences.getUsers() ?: arrayOf()

        if (user != null) {
            editUserName.setText(user.username)
            editPassWord.setText(user.password)
            editFullName.setText(user.fullname)
            editEmail.setText(user.email)
        }



        // Save changes
        btnSave!!.setOnClickListener {
            // check any empty field
            if (editUserName!!.text.isEmpty() || editPassWord!!.text.isEmpty() || editFullName!!.text.isEmpty() || editEmail!!.text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val username = editUserName!!.text.toString()
            val password = editPassWord!!.text.toString()
            val fullname = editFullName!!.text.toString()
            val email = editEmail!!.text.toString()
            val newUser = User(username, password, fullname, email)

            // Update user in SharedPreferences
            // Retrieve the list of saved users
            val userPreferences = UserPreferences(this)
            val savedUsers = userPreferences.getUsers() ?: arrayOf()
            //if it exist, show toast message
            // Check if the username or email already exists
            var userExists = false
            for (savedUser in savedUsers) {
                if (savedUser.username == username) {
                    userExists = true
                    editUserName!!.error = "Username already exists"
                    break
                }
                if (savedUser.email == email) {
                    userExists = true
                    editEmail!!.error = "Email already exists"
                    break
                }
            }

            // If no match is found, save the new user
            if (!userExists) {
                var newUsersList: Array<User> = emptyArray()

                for (savedUser in savedUsers) {
                    if (user != null) {
                        if (savedUser.username != user.username) {
                            newUsersList = newUsersList.plus(savedUser)
                        }
                    }
                }
                // Save the updated user list in UserPreferences
                newUsersList.plus(newUser) // Add the new user to the list
                userPreferences.addUser(newUsersList)  // Save the updated list
                //show toasted message
                Toast.makeText(this, "User is edited successfully", Toast.LENGTH_SHORT).show()
                editUserName!!.setText("")
                editPassWord!!.setText("")
                editFullName!!.setText("")
                editEmail!!.setText("")

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



        }

        btnBack.setOnClickListener {
            val intent = Intent(this, SearchAct::class.java)
            startActivity(intent)
        }
    }

        // Reset changes

        // Update user in SharedPreferences
}
