package com.github.hattamaulana.moviecatalogue.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.CATEGORY
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.GENRE
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.MOVIE_ID
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.OVERVIEW
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.POSTER_PATH
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.RATING
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.TITLE
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Genre.GENRE_ID
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Genre.NAME

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
        db?.execSQL(SQL_CREATE_TABLE_GENRE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.Favorites.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.Genre.TABLE_NAME}")

        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        private const val DATABASE_NAME = "db_movie_catalogue"
        private const val DATABASE_VERSION = 2

        private val SQL_CREATE_TABLE_MOVIE = """
            CREATE TABLE ${DatabaseContract.Favorites.TABLE_NAME} (
                $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $MOVIE_ID INTEGER NOT NULL,
                $TITLE TEXT NOT NULL, 
                $OVERVIEW TEXT NOT NULL,
                $POSTER_PATH TEXT NOT NULL,
                $RATING REAL NOT NULL,
                $CATEGORY TEXT NOT NULL,
                $GENRE TEXT NOT NULL
            )
        """.trimIndent()

        private val SQL_CREATE_TABLE_GENRE = """
            CREATE TABLE ${DatabaseContract.Genre.TABLE_NAME} (
                $_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $GENRE_ID REAL NOT NULL, 
                $NAME TEXT NOT NULL,
                $CATEGORY TEXT NOT NULL
            )
        """.trimIndent()
    }
}