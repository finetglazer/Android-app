package com.map.hung.myapplication.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User


class AddViewAct : Activity() {



    private var txtUsername: EditText? = null
    private var  txtPassword: EditText? = null
    private var txtFullname: EditText? = null
    private var txtEmail: EditText? = null
    private var btnAdd: Button? = null
    private var btnBack: TextView? = null


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
            val user = User(username, password, fullname, email)

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
            }
        }

        btnBack!!.setOnClickListener {
            val intent = Intent(this, UserHomeAct::class.java)
            startActivity(intent)
        }
    }
}


