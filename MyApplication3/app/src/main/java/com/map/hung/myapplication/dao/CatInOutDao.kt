package com.map.hung.myapplication.dao

import android.content.Context
import com.map.hung.myapplication.model.CatInOut

class CatInOutDao (context: Context) {

    private val dbHelper: DbHelper = DbHelper(context)

    // find id of CatInOut by id of Category and id of InOut

    fun findIdCatInOut(idCat: Int, idInOut: Int): CatInOut? {
        val db = dbHelper.readableDatabase

        // Query to get the id of CatInOut by id of Category and id of InOut
        val query = """
        SELECT id
        FROM tblCatInOut
        WHERE idCat = ? AND idInOut = ?
    """

        val cursor = db.rawQuery(query, arrayOf(idCat.toString(), idInOut.toString()))

        val idCatInOut = if (cursor.moveToNext()) {
            cursor.getInt(0)
        } else {
            -1
        }

        cursor.close()
        return if (idCatInOut != -1) CatInOut(idCatInOut) else null
    }

    // find id of IdInOut by id of CatInOut
    fun findIdInOut(idCatInOut: Int): Int {
        val db = dbHelper.readableDatabase

        // Query to get the id of InOut by id of CatInOut
        val query = """
        SELECT idInOut
        FROM tblCatInOut
        WHERE id = ?
    """

        val cursor = db.rawQuery(query, arrayOf(idCatInOut.toString()))

        val idInOut = if (cursor.moveToNext()) {
            cursor.getInt(0)
        } else {
            -1
        }

        cursor.close()
        return idInOut
    }


}