package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User
import com.map.hung.myapplication.activity.UserHomeAct

class LoginAct : Activity() {

    // Declaring UI components
    private var txtUsername: EditText? = null
    private var txtPassword: EditText? = null
    private var btnLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Assuming the XML layout file is named login.xml

        // Initializing UI components
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Setting onClickListener for the login button
        btnLogin?.setOnClickListener {
            onClick()
        }
    }

    // Method to handle login button click
    private fun onClick() {
        // Retrieving values from EditTexts

        val user = User(txtUsername?.text.toString(), txtPassword?.text.toString())

        // Checking if the credentials match
        if (user.username.toString().lowercase() == "b21dccn419" && user.password == "10122003") {
            val intent = Intent(this, UserHomeAct::class.java)
            // pass user to UserHomeAct
            intent.putExtra("user", user)
            startActivity(intent)
        } else {

            // If credentials don't match, show error message
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }
}
