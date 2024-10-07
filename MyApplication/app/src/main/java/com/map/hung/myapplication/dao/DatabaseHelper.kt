import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    context.getExternalFilesDir(null)?.absolutePath + "/example.db",
    null,
    1
)

 {
    override fun onCreate(db: SQLiteDatabase) {// is only called for the fist time creating database
//        db.execSQL("CREATE TABLE example (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {// is called when the version of the database is changed


    }

    companion object {
        fun getDatabase(context: Context): SQLiteDatabase {
            return SQLiteDatabase.openDatabase(
                context.getExternalFilesDir(null)?.absolutePath + "/example.db",
                null,
                SQLiteDatabase.OPEN_READWRITE
            )
        }
    }

}
