package com.github.hattamaulana.moviecatalogue.helper

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.CATEGORY
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.GENRE
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.MOVIE_ID
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.OVERVIEW
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.POSTER_PATH
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.RATING
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.TABLE_NAME
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.TITLE
import com.github.hattamaulana.moviecatalogue.database.SQLHelper
import com.github.hattamaulana.moviecatalogue.model.DataModel

class FavoriteHelper(context: Context) : SQLHelper<DataModel>(context) {

    override val table: String = TABLE_NAME
    override val sortBy: String = TITLE
    override val _id: String = MOVIE_ID

    override fun refactor(cursor: Cursor): DataModel {
        val ids = cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
        val posterPath = cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH))
        val overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW))
        val rating  = cursor.getDouble(cursor.getColumnIndexOrThrow(RATING))
        val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
        val genre = cursor.getString(cursor.getColumnIndexOrThrow(GENRE))
        val data = DataModel(ids, posterPath, title, overview, genre, rating, null)
        data.category = category

        return data
    }

    companion object {
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FavoriteHelper(context)
                    }
                }
            }

            return INSTANCE as FavoriteHelper
        }
    }

}