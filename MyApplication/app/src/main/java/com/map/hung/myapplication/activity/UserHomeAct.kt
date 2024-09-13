package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User

class UserHomeAct : Activity() {

    private lateinit var txtHello: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnAddUser: Button
    private lateinit var btnEditUser: Button
    private lateinit var btnDeleteUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userhome)
        // Get the username passed from LoginAct
        txtHello = findViewById(R.id.txtHello)
        // get user model from intent
        val user = intent.getParcelableExtra<User>("user")

        if (user != null) {
            txtHello.text = hello(user.username ?: "User")
        }

        // Logout button
        btnLogout = findViewById(R.id.btnLogoutuser)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LogoutAct::class.java)
            startActivity(intent)
        }
        // Add user button
        btnAddUser = findViewById(R.id.btnAddUser)
        btnAddUser.setOnClickListener {
            val intent = Intent(this, AddViewAct::class.java)
            startActivity(intent)
        }
        // Edit user button
        btnEditUser = findViewById(R.id.btnEditUser)
        btnEditUser.setOnClickListener {
            val intent = Intent(this, SearchAct::class.java)
            startActivity(intent)
        }

//         Delete user button
        btnDeleteUser = findViewById(R.id.btnDeleteUser)
        btnDeleteUser.setOnClickListener {
            val intent = Intent(this, SearchAct::class.java)
            startActivity(intent)
        }



    }
    // Function to create a hello message
    private fun hello(username: String): String {
        return "Hello, $username!"
    }



}
