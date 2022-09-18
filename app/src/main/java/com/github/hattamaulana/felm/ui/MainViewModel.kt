package com.github.hattamaulana.felm.ui

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.felm.data.MovieRepository
import com.github.hattamaulana.felm.data.remote.MovieDbRepository
import com.github.hattamaulana.felm.data.local.*
import com.github.hattamaulana.felm.data.model.DataGenreRelation
import com.github.hattamaulana.felm.data.model.DataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Context,
    private val repository: MovieRepository,
) : ViewModel() {

    private var repo: MovieDbRepository = MovieDbRepository(context)
    private var appDb: AppDatabase = DatabaseHelper.openDb(context)
    private var relationDao: DataGenreDao = appDb.relationDataAndGenreDao()
    private var favoriteDao: FavoriteDao = appDb.favoriteDao()
    private var genreDao: GenreDao = appDb.genreDao()

    private val _catalogueTotalPage = MutableLiveData<Int>()
    private val sortBy = MutableLiveData<Map<String, Int>>()

    val catalogues = MutableLiveData<List<DataModel>>()
    val favorites = MutableLiveData<List<DataModel>>()
    val catalogueTotalPage = _catalogueTotalPage.value
    var catalogeStatePosition: Int = 0

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

            Log.d("TAG", "onCreate: tag=$list")

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