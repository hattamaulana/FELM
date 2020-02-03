package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.data.model.DataModel

class CatalogueViewModel : ViewModel() {

    private lateinit var repo: MovieDbRepository

    private val _totalPage = MutableLiveData<Int>()

    val totalPage = _totalPage.value
    val listData = MutableLiveData<List<DataModel>>()

    var context: Context? = null
        set(value) {
            repo = MovieDbRepository(value as Context)
            field = value
        }

    fun loadData(tag: String) =
        repo.getDiscover(tag, 1) { list, total ->
            _totalPage.postValue(total)
            listData.postValue(list)
        }

    fun loadMore(tag: String, page: Int, callback: () -> Unit) =
        repo.getDiscover(tag, page) { list, _ ->
            val newList = if (page > 1) arrayListOf<DataModel>().apply {
                addAll(listData.value!!)
                addAll(list)
            } else {
                list
            }

            listData.postValue(newList)
            callback()
        }
}