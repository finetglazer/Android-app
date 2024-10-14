package com.map.hung.myapplication.dao

import android.content.ContentValues
import android.content.Context
import com.map.hung.myapplication.model.Category
import android.database.Cursor
import com.map.hung.myapplication.model.InOut

class CategoryDao(context: Context) {

    private val dbHelper: DbHelper = DbHelper(context)

    fun searchByInOut(check: Boolean): List<String> {
        val db = dbHelper.readableDatabase

        // Determine the inOut type (1 for "thu", 2 for "chi")
        val inOutType = if (check) 1 else 2

        // Query to get the names of the categories
        val query = """
        SELECT c.name
        FROM tblCategory c
        INNER JOIN tblCatInOut cio ON c.id = cio.idCat
        INNER JOIN tblInOut io ON cio.idInOut = io.id
        WHERE io.id = ?
    """

        val cursor: Cursor = db.rawQuery(query, arrayOf(inOutType.toString()))

        val categoryNames = mutableListOf<String>()
        while (cursor.moveToNext()) {
            categoryNames.add(cursor.getString(0))  // Add the category name to the list
        }
        cursor.close()
        return categoryNames
    }

    // search CateGory by name
    fun searchByName(name: String): Category? {
        val db = dbHelper.readableDatabase

        // Query to get the category by name
        // Get all information of the category
        val query = """
        SELECT id, name
        FROM tblCategory
        WHERE name = ?
    """

        val cursor: Cursor = db.rawQuery(query, arrayOf(name))

        if (cursor.moveToNext()) {
            val category = Category(
                id = cursor.getInt(0),
                name = cursor.getString(1)


            )
            cursor.close()
            return category
        }
        cursor.close()
        return null
    }

    // save
    fun insert(name: String, icon: String, parent: String, inOut: Boolean) {
        val db = dbHelper.writableDatabase

        // find id of parent category
        val parentId = searchByName(parent)?.id
        //
        val values = ContentValues().apply { // Create a new ContentValues object
            put("name", name)  // Put the name of the category
            put("icon", icon)  // Put the icon of the category
            put("idParent", parentId)  // Put the id of the parent category
        }

        // Insert the category
        db.insert("tblCategory", null, values)

        // Get the id of the category
        val categoryId = searchByName(name)?.id

        // Insert the inOut type
        val inOutType = if (inOut) 1 else 2
        val insertInOut = "INSERT INTO tblCatInOut (idCat, idInOut) VALUES (?, ?)"

        // Insert the inOut type
        db.execSQL(insertInOut, arrayOf(categoryId, inOutType))

    }


}
