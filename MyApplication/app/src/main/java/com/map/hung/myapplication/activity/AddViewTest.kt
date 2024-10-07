package com.map.hung.myapplication.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.UserDao
import com.map.hung.myapplication.model.User
import java.util.Date

class AddViewTest : AppCompatActivity() {
    private lateinit var userDao: UserDao

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDao = UserDao(this)

        // Add a new user

        val user = User(
            username = "hung",
            password = "123456",
            fullname = "Nguyen Van Hung",
            email = "test@gmail.com",
            dob = Date(),
            gender = "male",
            phone = "0123456789"
        )

        val userId = userDao.addUser(user)
        if (userId > 0) {
            // User added successfully
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
        } else {
            // Failed to add user
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
        }

        val db = DatabaseHelper.getDatabase(this)

        val cursor = db.rawQuery("SELECT * FROM tbUser", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val username = cursor.getString(cursor.getColumnIndex("username"))
                val password = cursor.getString(cursor.getColumnIndex("password"))
                val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val tel = cursor.getString(cursor.getColumnIndex("tel"))
                val dob = cursor.getLong(cursor.getColumnIndex("dob"))
                Log.d("Database", "ID: $id, Username: $username, Password: $password, Fullname: $fullname, Email: $email, Tel: $tel, DOB: $dob")
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}