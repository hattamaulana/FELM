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

        dbHelper.open()

        val check = dbHelper.getAll().filter { item ->
            item.id == data.id
        }

        return if (check.isEmpty()){
            dbHelper.insert(value)
            dbHelper.close()
            true
        } else {
            dbHelper.close()
            false
        }
    }

    fun delete(id: Int) {
        val dbHelper = FavoriteHelper.getInstance(context as Context)
        dbHelper.open()
        dbHelper.delete(id.toString())
        dbHelper.close()
    }
}