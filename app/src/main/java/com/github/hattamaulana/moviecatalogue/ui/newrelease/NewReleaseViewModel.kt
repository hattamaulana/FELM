package com.github.hattamaulana.moviecatalogue.ui.newrelease

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import java.text.SimpleDateFormat
import java.util.*

class NewReleaseViewModel : ViewModel() {

    private val _totalPage =  MutableLiveData<Int>()

    private lateinit var repository: MovieDbRepository
    private lateinit var type: String

    val data = MutableLiveData<List<DataModel>>()
    val totalPage = _totalPage.value

    fun init(context: Context, tag: String) {
        type = tag
        repository = MovieDbRepository(context).apply {
            newRelease(type, getDate()) { list, totalPage ->
                _totalPage.postValue(totalPage)
                data.postValue(list)
            }
        }
    }

    fun loadMore(page: Int, callback: () -> Unit) {
        repository.newRelease(type, getDate(), page.toString()) { list, _ ->
            val newList = if (page > 1) arrayListOf<DataModel>().apply {
                addAll(data.value!!)
                addAll(list)
            } else {
                list
            }

            data.postValue(newList)
        }
        callback()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val pattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(Date())
    }
}