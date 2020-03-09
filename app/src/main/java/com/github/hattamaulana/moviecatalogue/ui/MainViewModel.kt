package com.github.hattamaulana.moviecatalogue.ui

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.data.database.*
import com.github.hattamaulana.moviecatalogue.data.model.DataGenreRelation
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private lateinit var repo: MovieDbRepository
    private lateinit var appDb: AppDatabase
    private lateinit var relationDao: DataGenreDao
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var genreDao: GenreDao

    private val _viewStateCatalogue = MutableLiveData<Int>()
    private val _catalogueTotalPage = MutableLiveData<Int>()
    private val sortBy = MutableLiveData<Map<String, Int>>()

    val catalogues = MutableLiveData<List<DataModel>>()
    val favorites = MutableLiveData<List<DataModel>>()
    val catalogueTotalPage = _catalogueTotalPage.value
    val viewStateCatalogue = _viewStateCatalogue.value

    var context: Context? = null
        set(value) {
            repo = MovieDbRepository(value as Context)
            appDb = DatabaseHelper.openDb(value as Context)
            genreDao = appDb.genreDao()
            favoriteDao = appDb.favoriteDao()
            relationDao = appDb.relationDataAndGenreDao()
            field = value
        }

    fun setViewStateCatalogue(position: Int) {
        _viewStateCatalogue.postValue(position)
    }

    fun loadCatalogue(tag: String, sort: Int, page: Int = 1, callback: (() -> Unit)? = null) {
        repo.getDiscover(tag, sort, page) { list, totalPage ->
            _catalogueTotalPage.postValue(totalPage)
            val newList = arrayListOf<DataModel>().apply {
                if (page != 1) {
                    catalogues.value?.takeIf { it.isNotEmpty() }?.let { addAll(it) }
                }
                addAll(list)
            }

            catalogues.postValue(newList)
            callback?.invoke()
        }
    }

    fun getSortBy(tag: String): Int? =
        sortBy.value?.filterKeys { it == tag }?.get(tag)

    fun setSortBy(tag: String, sort: Int) {
        sortBy.postValue(mapOf(tag to sort))
    }

    fun loadFavorites(tag: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val list = favoriteDao.getAllAsync()
                .filter { data -> data.category == tag }
                .apply {
                    forEach { data ->
                        data.genres = mutableListOf<Int>().apply {
                            relationDao.find(data.id as Int).forEach { add(it.genreId) }
                        }
                    }
                }

            favorites.postValue(list)
        }

    fun removeDataFromFavorite(arg: DataModel) {
        CoroutineScope(Dispatchers.IO).launch { favoriteDao.remove(arg) }
        CoroutineScope(Dispatchers.IO).launch {
            val id = arg.id as Int
            val genreIds = arg.genres
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .apply { action = AppWidgetManager.ACTION_APPWIDGET_UPDATE }
            context?.sendBroadcast(intent)
            genreIds?.forEach { gId -> relationDao.remove(DataGenreRelation(id, gId)) }
        }
    }
}