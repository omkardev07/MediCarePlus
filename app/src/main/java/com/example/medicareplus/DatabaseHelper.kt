package com.example.medicareplus

import android.content.*
import android.database.Cursor
import android.database.sqlite.*

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MedicineDB", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE medicines(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, time TEXT, status TEXT DEFAULT 'Pending')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE medicines ADD COLUMN status TEXT DEFAULT 'Pending'")
        }
    }

    fun insertMedicine(name: String, time: String, status: String = "Pending"): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("time", time)
        values.put("status", status)
        return db.insert("medicines", null, values) != -1L
    }

    fun updateStatus(name: String, status: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("status", status)
        db.update("medicines", values, "name=?", arrayOf(name))
    }

    fun getAll(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM medicines", null)
    }
}
