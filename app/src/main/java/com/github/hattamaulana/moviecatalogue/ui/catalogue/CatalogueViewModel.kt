package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.api.TheMovieDbFactory.Callback
import com.github.hattamaulana.moviecatalogue.api.TheMovieDbRepository
import com.github.hattamaulana.moviecatalogue.model.DataModel

class CatalogueViewModel : ViewModel() {

    var context: Context? = null

    private val TAG = this.javaClass.simpleName

    private var listData = MutableLiveData<ArrayList<DataModel>>()

    fun getData(tag: String): LiveData<ArrayList<DataModel>> {
        val repo = TheMovieDbRepository(context!!)

        repo.getDiscover(tag, object : Callback {
            override fun getData(p0: ArrayList<DataModel>) {
                listData.postValue(p0)

                Log.d(TAG, "getData: ...")
            }
        })

        return listData
    }
}