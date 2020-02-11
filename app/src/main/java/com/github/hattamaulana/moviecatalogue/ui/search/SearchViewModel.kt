package com.github.hattamaulana.moviecatalogue.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.data.database.AppDbProvider
import com.github.hattamaulana.moviecatalogue.data.database.FavoriteDao
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private lateinit var repo: MovieDbRepository
    private lateinit var database: FavoriteDao

    /** use variabel below to observe data */
    var totalPage = MutableLiveData<Int>()
    var listResult = MutableLiveData<List<DataModel>>()

    /** Set Context for set context */
    var context: Context? = null
        set(value) {
            field = value
            repo  = MovieDbRepository(value as Context)
            database = AppDbProvider.getDb(value).favoriteDao()
        }

    fun searchLocal(tag: String, text: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val query = "%$text%"
            val result = database.findByTitle(query, tag)

            listResult.postValue(result)
        }

    /** use to search data from api */
    fun searchApi(tag: String, text: String, page: Int = 1, callback: (() -> Unit)? = null) =
        repo.search(tag, text) { list, _totalPage ->
            totalPage.postValue(_totalPage)

            val newList = if (page > 1) arrayListOf<DataModel>().apply {
                addAll(listResult.value!!)
                addAll(list)
            } else {
                list
            }

            listResult.postValue(newList)
            if (callback != null) callback()
        }

}