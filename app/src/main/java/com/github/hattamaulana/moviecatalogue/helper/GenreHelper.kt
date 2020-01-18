package com.github.hattamaulana.moviecatalogue.helper

import android.content.Context
import android.database.Cursor
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Genre.CATEGORY
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Genre.GENRE_ID
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Genre.NAME
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Genre.TABLE_NAME
import com.github.hattamaulana.moviecatalogue.database.SQLHelper
import com.github.hattamaulana.moviecatalogue.model.GenreModel

class GenreHelper(context: Context) : SQLHelper<GenreModel>(context) {

    override val table: String = TABLE_NAME
    override val sortBy: String = GENRE_ID
    override val _id: String = GENRE_ID

    override fun refactor(cursor: Cursor): GenreModel {
        val ids = cursor.getInt(cursor.getColumnIndexOrThrow(GENRE_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
        val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
        val genre = GenreModel(ids, name)
        genre.category = category

        return genre
    }
}