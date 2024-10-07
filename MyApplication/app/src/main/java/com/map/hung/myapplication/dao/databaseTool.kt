package com.map.hung.myapplication.dao

import DatabaseHelper
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.map.hung.myapplication.R

fun connectDatabase(context: Context): SQLiteDatabase {
    val dbPath = context.getExternalFilesDir(null)?.absolutePath + "/example.db"
    return SQLiteDatabase.openDatabase(
        dbPath,
        null,
        SQLiteDatabase.OPEN_READWRITE
    )
}

fun delete(db: SQLiteDatabase, tableName: String) {
    db.execSQL("DROP TABLE IF EXISTS $tableName")
}
class MyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase  // This will create or open the database

        connectDatabase(this);
//        delete(db, "tbUser")
//        addNewTable(this)
//        deleteNewTable(this)
    }

}