package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.data.database.*
import com.github.hattamaulana.moviecatalogue.data.model.DataGenreRelation
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
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

                        Log.d(TAG, "loadData: id=${data.id} genres=${data.genres}")
                    }
                }

            listData.postValue(favorites)
        }

        return listData
    }

    fun remove(arg: DataModel) {
        launch { favoriteDao.remove(arg) }
        launch {
            val id = arg.id as Int
            val genreIds = arg.genres

            genreIds?.forEach { gId -> relationDao.remove(DataGenreRelation(id, gId)) }
        }
    }
}