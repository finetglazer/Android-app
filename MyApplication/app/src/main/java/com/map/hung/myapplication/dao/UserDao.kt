package com.map.hung.myapplication.dao

import DatabaseHelper
import android.content.ContentValues
import android.content.Context
import com.map.hung.myapplication.model.User
import java.text.SimpleDateFormat
import java.util.Locale

class UserDao(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    // Create a new user

    fun addUser(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("password", user.password)
            put("fullname", user.fullname)
            put("email", user.email)
            put("phone", user.phone)


            val dateformatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dobString = dateformatter.format(user.dob)

            put("dob", dobString)
            put("gender", user.gender)

        }

        return db.insert("tbUser", null, values)
    }

    // check if username and email is the same

    // Check if a user exists by username or email
    fun userExists(username: String, email: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tbUser",
            arrayOf("username", "email"), // Columns to return
            "username = ? OR email = ?", // Selection
            arrayOf(username, email), // Selection args
            null, // Group by
            null, // Having
            null // Order by
        )

        val exists = cursor.moveToFirst() // If there are results, cursor will be at first
        cursor.close() // Always close the cursor
        return exists
    }

    fun userUserNameExists(username: String) : Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tbUser",
            arrayOf("username"), // Columns to return
            "username = ?", // Selection
            arrayOf(username), // Selection args
            null, // Group by
            null, // Having
            null // Order by
        )

        val exists = cursor.moveToFirst() // If there are results, cursor will be at first
        cursor.close() // Always close the cursor
        return exists
    }

    fun userEmailExists(email: String) : Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tbUser",
            arrayOf("email"), // Columns to return
            "email = ?", // Selection
            arrayOf(email), // Selection args
            null, // Group by
            null, // Having
            null // Order by
        )

        val exists = cursor.moveToFirst() // If there are results, cursor will be at first
        cursor.close() // Always close the cursor
        return exists
    }

    // Update a user
    fun updateUser(user: User, newUser: User): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("username", newUser.username)
            put("password", newUser.password)
            put("fullname", newUser.fullname)
            put("email", newUser.email)
            put("phone", newUser.phone)
            put("gender", newUser.gender)

            val dateformatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dobString = dateformatter.format(user.dob)

            put("dob", dobString)


        }

        return db.update(
            "tbUser",
            values,
            "username = ?",
            arrayOf(user.username)
        )
    }

    // Search by username and email and full name
    fun searchUsers(search: String): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tbUser",
            arrayOf("username", "password", "fullname", "email", "dob", "gender", "phone"),
            "username LIKE ? OR email LIKE ? OR fullname LIKE ?",
            arrayOf("%$search%", "%$search%", "%$search%"),
            null,
            null,
            null
        )

        val users = mutableListOf<User>()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        while (cursor.moveToNext()) {
            val user = User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                // parse string to date
                dateFormatter.parse(cursor.getString(4)),
                cursor.getString(5),
                cursor.getString(6)
            )
            users.add(user)
        }


        cursor.close()
        return users

    }

    // delete a user
    fun deleteUser(user: User): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            "tbUser",
            "username = ?",
            arrayOf(user.username)
        )
    }



}