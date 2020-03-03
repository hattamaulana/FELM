package com.github.hattamaulana.moviecatalogue.data.database

import android.content.Context
import android.database.Cursor
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.github.hattamaulana.moviecatalogue.data.model.*

object DatabaseHelper {

    private lateinit var appDb: AppDatabase
    private lateinit var sqlite: SupportSQLiteOpenHelper

    private var database: AppDatabase? = null

    fun openDb(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context, AppDatabase::class.java, "app_database.db")
                .build()
        }

        return database as AppDatabase
    }

    fun openHelper(context: Context) {
        appDb = openDb(context)
        sqlite = appDb.openHelper
    }

    fun getGenreById(id: String): Cursor {
        val executor = sqlite.readableDatabase
        val query = """
            SELECT * FROM $RELATION_TABLE_NAME 
            INNER JOIN $GENRE_TABLE_NAME
                ON $RELATION_TABLE_NAME.$RELATION_GENRE_ID = $GENRE_TABLE_NAME.$GENRE_ID
            WHERE $RELATION_TABLE_NAME.$RELATION_DATA_ID = $id
        """.trimIndent()
        return executor.query(query)
    }

    fun getDataById(id: String): Cursor {
        val executor = sqlite.readableDatabase
        val query = """
            SELECT * FROM $DATA_TABLE_NAME 
            WHERE $DATA_ID = $id
        """.trimIndent()
        return executor.query(query)
    }

    fun getAll(): Cursor {
        val executor = sqlite.readableDatabase
        val query = "SELECT * FROM $DATA_TABLE_NAME"
        return executor.query(query)
    }

    fun remove(whereClause: String, whereArgs: Array<String>): Int {
        val executor = sqlite.writableDatabase
        return executor.delete(DATA_TABLE_NAME, whereClause, whereArgs)
    }
}