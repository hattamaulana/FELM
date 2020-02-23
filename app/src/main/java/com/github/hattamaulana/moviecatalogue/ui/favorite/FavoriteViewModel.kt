package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.database.*
import com.github.hattamaulana.moviecatalogue.data.model.DataGenreRelation
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.widget.FavoriteWidgetProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoriteViewModel : ViewModel(), CoroutineScope {

    private lateinit var appDb: AppDatabase
    private lateinit var relationDao: DataGenreDao
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var genreDao: GenreDao

    private val TAG = this.javaClass.simpleName
    private val listData = MutableLiveData<List<DataModel>>()

    var context: Context? = null
        set(value) {
            appDb = AppDbProvider.getDb(value as Context)
            field = value
            genreDao = appDb.genreDao()
            favoriteDao = appDb.favoriteDao()
            relationDao = appDb.relationDataAndGenreDao()
        }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    /**
     *
     */
    fun loadData(tag: String): LiveData<List<DataModel>> {
        launch {
            val favorites = favoriteDao.all()
                .filter { data -> data.category == tag }
                .apply {
                    forEach { data ->
                        data.genres = mutableListOf<Int>().apply {
                            relationDao.find(data.id as Int).forEach { add(it.genreId) }
                        }
                    }
                }

            listData.postValue(favorites)
        }

        return listData
    }

    fun remove(arg: DataModel) {
        launch { favoriteDao.remove(arg) }
        launch {
            Log.d(TAG, "remove: OK; Success Delete Favorites")

            val id = arg.id as Int
            val genreIds = arg.genres
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .apply { action = AppWidgetManager.ACTION_APPWIDGET_UPDATE }
            context?.sendBroadcast(intent)
            genreIds?.forEach { gId -> relationDao.remove(DataGenreRelation(id, gId)) }
        }
    }
}