package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.map.hung.myapplication.R

class LogoutAct : Activity() {

    private var btnLogout: Button? = null
    private var btnCancel: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout) // Assuming the XML layout file is named logout.xml

        // Logout logic here
        btnLogout = findViewById(R.id.btnLogout)
        btnCancel = findViewById(R.id.btnCancel)
        if (btnLogout != null) {
            btnLogout?.setOnClickListener {
                btnLogoutOnClick();
            }
        }
        if (btnCancel != null) {
            btnCancel?.setOnClickListener {
                btnCancelOnClick();
            }
        }

    }

    private fun btnLogoutOnClick() {
        // btn logOUT -> back to login screen
        val intent = Intent(this, LoginAct::class.java)
        startActivity(intent)
    }

    private fun btnCancelOnClick() {
        // btn cancel -> back to home screen
        val intent = Intent(this, UserHomeAct::class.java)
        startActivity(intent)
    }
}