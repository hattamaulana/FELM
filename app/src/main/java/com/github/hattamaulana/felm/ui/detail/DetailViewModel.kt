package com.github.hattamaulana.felm.ui.detail

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.felm.data.remote.MovieDbRepository
import com.github.hattamaulana.felm.data.local.*
import com.github.hattamaulana.felm.data.model.DataGenreRelation
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.model.GenreModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class DetailViewModel : ViewModel(), CoroutineScope {

    private lateinit var appDb: AppDatabase
    private lateinit var relationDao: DataGenreDao
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var genreDao: GenreDao
    private lateinit var repo: MovieDbRepository

    private val TAG = this.javaClass.simpleName

    val listSimilarContent = MutableLiveData<List<DataModel>>()

    var context: Context? = null
        set(value) {
            appDb = DatabaseHelper.openDb(value as Context)
            field = value

            relationDao = appDb.relationDataAndGenreDao()
            favoriteDao = appDb.favoriteDao()
            genreDao = appDb.genreDao()
            repo = MovieDbRepository(value)
        }

    override val coroutineContext: CoroutineContext
        get() = IO

    /** Digunakan untuk menambahkan data movie atau tv show ke database favorite. */
    fun save(arg: DataModel) {
        launch { favoriteDao.add(arg) }
        launch {
            val id = arg.id as Int
            val genreIds = arg.genres
            updateWidget("Save")
            genreIds?.forEach { gId -> relationDao.add(DataGenreRelation(id, gId)) }
        }
    }

    /** Digunakan untuk menghapus data movie atau tv yang telah terdaftar di database favorite. */
    fun delete(arg: DataModel) {
        launch { favoriteDao.remove(arg) }
        launch {
            val id = arg.id as Int
            val genreIds = arg.genres
            updateWidget("Remove")
            genreIds?.forEach { gId -> relationDao.remove(DataGenreRelation(id, gId)) }
        }
    }

    /** Function ini untuk melakukan checking data genre berdasarkan id. */
    fun checkData(id: Int, callback: (arg: Boolean) -> Unit) = launch {
        val task = favoriteDao.findByIdAsync(id).isEmpty()
        Log.d(TAG, "checkData: task result=$task")

        launch(Main) { callback(task) }
    }

    /** function ini digunakan untuk menambil data genre base on id. */
    fun findGenreByIdAsync(id: Int, callback: (arg: GenreModel) -> Unit) = launch {
        val genre = genreDao.findById(id)
        Log.d(TAG, "findGenreByIdAsync: id=$id; genre=$genre")

        if (genre.isNotEmpty()) {
            launch(Main) { callback(genre[0]) }
        }
    }

    /** Get Similar Movie or Tv Show */
    fun getSimilarContent(tag: String?, id: Int?) {
        repo.similarContent(tag, id) { listData, _ -> listSimilarContent.postValue(listData) }
    }

    /** Update Widget when adding or removing data */
    private fun updateWidget(string: String? = "") {
        Log.d(TAG, "remove: OK; Success $string Favorites")

        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .apply { action = AppWidgetManager.ACTION_APPWIDGET_UPDATE }
        context?.sendBroadcast(intent)
    }

    fun getDataFavorite(id: Int, callback: (data: DataModel) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val genreIds = arrayListOf<Int>()

            relationDao.find(id).forEach { id ->
                genreIds.add(id.genreId)
            }

            val data = favoriteDao.findByIdAsync(id)[0]
                .apply { genres = genreIds }

            launch(Dispatchers.Main) { callback(data) }
        }
    }
}