package com.map.hung.myapplication.activity

import DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.map.hung.myapplication.R
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the database helper
//        dbHelper = ExternalDatabaseHelper(this)

        // Test database connection
        testDatabaseConnection()
    }

    @SuppressLint("Range")
    private fun testDatabaseConnection() {
//        val db = DatabaseHelper.getDatabase(context)
        val db = DatabaseHelper.getDatabase(this)
// Now you can perform operations using the 'db' object


        // Insert data into the database
//        val insertSQL = "INSERT INTO example (name) VALUES ('manh hung')"
//        db.execSQL(insertSQL)

//         Query data from the database

        // create a table user
//                db.execSQL("CREATE TABLE tbUser (\n" +
//                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
//                "    username INTEGER,\n" +
//                "    password VARCHAR(50),\n" +
//                "    fullname VARCHAR(50),\n" +
//                "    email VARCHAR(50),\n" +
//                "    tel VARCHAR(15),\n" +
//                "    dob DATETIME,\n" +
//                "    gender VARCHAR(15)\n" +
//                ");\n" +
//                "")


        val cursor = db.rawQuery("SELECT * FROM example", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                Log.d("Database", "ID: $id, Name: $name")
            } while (cursor.moveToNext())
        } else {
            Log.d("Database", "No data found.")
        }
        cursor.close()

        // Close the database connection
        db.close()

        // Show a Toast message for success
        Toast.makeText(this, "Database connection tested successfully.", Toast.LENGTH_SHORT).show()
    }
}
