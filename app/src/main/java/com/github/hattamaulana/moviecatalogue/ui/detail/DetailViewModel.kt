package com.github.hattamaulana.moviecatalogue.ui.detail

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.MutableLiveData
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

    var context: Context? = null

    fun save(data: DataModel): Boolean {
        val dbHelper = FavoriteHelper.getInstance(context as Context)
        val value = ContentValues()
        value.put(MOVIE_ID, data.id)
        value.put(TITLE, data.title)
        value.put(POSTER_PATH, data.img)
        value.put(OVERVIEW, data.overview)
        value.put(RATING, data.rating)
        value.put(CATEGORY, data.category)
        value.put(GENRE, data.genres)

        return if (isExist(data.id as Int)) {
            dbHelper.open()
            dbHelper.insert(value)
            dbHelper.close()
            true
        } else {
            false
        }
    }

    fun isExist(id: Int) : Boolean {
        val dbHelper = FavoriteHelper.getInstance(context as Context)
        dbHelper.open()
        val list = dbHelper.getAll().filter { item -> item.id == id }
        dbHelper.close()

        return list.isEmpty()
    }

    fun delete(id: Int) {
        val dbHelper = FavoriteHelper.getInstance(context as Context)
        dbHelper.open()
        dbHelper.delete(id.toString())
        dbHelper.close()
    }
}