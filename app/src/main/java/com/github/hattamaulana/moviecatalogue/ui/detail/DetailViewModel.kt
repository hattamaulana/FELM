package com.github.hattamaulana.moviecatalogue.ui.detail

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.CATEGORY
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.GENRE
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.MOVIE_ID
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.OVERVIEW
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.POSTER_PATH
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.RATING
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract.Favorites.TITLE
import com.github.hattamaulana.moviecatalogue.helper.FavoriteHelper
import com.github.hattamaulana.moviecatalogue.model.DataModel

class DetailViewModel : ViewModel() {

    private lateinit var dbHelper: FavoriteHelper

    var context: Context? = null
        set(value) {
            dbHelper = FavoriteHelper.getInstance(value as Context)
            field = value
        }

    fun checkData(id: Int) : Boolean {
        dbHelper.open()

        val result = dbHelper.getAll().filter { item -> item.id == id }

        dbHelper.close()

        return result.isEmpty()
    }

    fun save(data: DataModel): Boolean {
        return if (checkData(data.id as Int)) {
            val value = ContentValues()
            value.put(MOVIE_ID, data.id)
            value.put(TITLE, data.title)
            value.put(POSTER_PATH, data.img)
            value.put(OVERVIEW, data.overview)
            value.put(RATING, data.rating)
            value.put(CATEGORY, data.category)
            value.put(GENRE, data.genres)

            dbHelper.open()
            dbHelper.insert(value)
            dbHelper.close()
            true
        } else {
            false
        }
    }

    fun delete(id: Int) {
        dbHelper.open()
        dbHelper.delete(id.toString())
        dbHelper.close()
    }
}