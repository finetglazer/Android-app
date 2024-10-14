package com.map.hung.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User

class AddEditUserActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var saveButton: Button
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.user_edit_form)

        nameInput = findViewById(R.id.userNameInput)
        emailInput = findViewById(R.id.userEmailInput)
        saveButton = findViewById(R.id.saveButton)

        user = intent.getParcelableExtra("user")!!
        user.let {
            nameInput.setText(it.name)
            emailInput.setText(it.email)
        }

        saveButton.setOnClickListener {
            onClick()
        }


    }

    fun onClick() {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        if (user != null) {
            user?.name = name
            user?.email = email
        } else {
            user = User(0, name, email) // New user
        }

        val intent = Intent().apply {
            putExtra("user", user)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}