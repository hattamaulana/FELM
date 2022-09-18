package com.github.hattamaulana.felm.ui.search

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.github.hattamaulana.android.core.common.BaseViewModel
import com.github.hattamaulana.felm.data.MovieRepository
import com.github.hattamaulana.felm.data.local.DatabaseHelper
import com.github.hattamaulana.felm.data.local.FavoriteDao
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbRepository
import com.github.hattamaulana.felm.data.remote.request.SearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseViewModel() {

    private lateinit var database: FavoriteDao

    /** use variabel below to observe data */
    var totalPage = MutableLiveData<Int>()
    var listResult = MutableLiveData<List<DataModel>>()

    fun searchLocal(tag: String, text: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val query = "%$text%"
            val result = database.findByTitle(query, tag)

            listResult.postValue(result)
        }

    /** use to search data from api */
    fun searchApi(
        tag: String,
        text: String,
        callback: (() -> Unit)? = null
    ) = movieRepository.search(tag, SearchRequest(text)).send { }

//    =
//        repo.search(tag, text) { list, _totalPage ->
//            totalPage.postValue(_totalPage)
//
//            val newList = if (page > 1) arrayListOf<DataModel>().apply {
//                addAll(listResult.value!!)
//                addAll(list)
//            } else {
//                list
//            }
//
//            listResult.postValue(newList)
//            if (callback != null) callback()
//        }

}