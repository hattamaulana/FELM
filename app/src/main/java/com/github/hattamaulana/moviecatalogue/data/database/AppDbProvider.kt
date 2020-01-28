package com.github.hattamaulana.moviecatalogue.data.database

import android.content.Context
import androidx.room.Room

/**
 * Object [AppDbProvider]
 * dapat digunakan sebagai instance dari AppDatabase.
 */

object AppDbProvider {

    private var database: AppDatabase? = null

    fun getDb(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context, AppDatabase::class.java, "app_database.db")
                .build()
        }

        return database as AppDatabase
    }

}