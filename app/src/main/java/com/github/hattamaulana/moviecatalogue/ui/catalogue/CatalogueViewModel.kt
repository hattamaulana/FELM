package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.data.model.DataModel

class CatalogueViewModel : ViewModel() {

    private lateinit var repo: MovieDbRepository

    private var listData = MutableLiveData<List<DataModel>>()

    var context: Context? = null
        set(value) {
            repo = MovieDbRepository(value as Context)
            field = value
        }

    fun getData(tag: String): LiveData<List<DataModel>> {
        repo.getDiscover(tag) { list -> listData.postValue(list) }

        return listData
    }
}