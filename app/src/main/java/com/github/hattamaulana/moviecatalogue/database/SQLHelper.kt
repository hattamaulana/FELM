package com.github.hattamaulana.moviecatalogue.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log

abstract class SQLHelper<T>(context: Context) {

    abstract val table: String
    abstract val sortBy: String
    abstract val _id: String

    abstract fun refactor(cursor: Cursor): T

    private lateinit var database: SQLiteDatabase

    private val TAG = this.javaClass.name
    private val dataBaseHelper = DatabaseHelper(context)

    @Throws(SQLException::class) fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen) {
            database.close()
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            table,
            null, null, null, null, null,
            "$sortBy ASC", null
        )
    }

    fun getAll(): ArrayList<T> {
        val arrayList = ArrayList<T>()
        val cursor = database.query(
            table,
            null, null, null, null, null,
            "$sortBy ASC", null
        )

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                arrayList.add(refactor(cursor))
                cursor.moveToNext()
            } while (! cursor.isAfterLast)
        }

        cursor.close()
        return arrayList
    }

    fun insert(values: ContentValues?): Long {
        Log.d(TAG, "insert: *")

        return database.insert(table, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        Log.d(TAG, "update: *")

        return database.update(table, values, "$sortBy = ?", arrayOf(id))
    }

    fun delete(id: String): Int {
        Log.d(TAG, "delete: *")

        return database.delete(table, "$_id = '$id'", null)
    }
}