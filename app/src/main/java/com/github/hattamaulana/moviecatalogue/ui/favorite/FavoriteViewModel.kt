package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.helper.FavoriteHelper
import com.github.hattamaulana.moviecatalogue.model.DataModel

class FavoriteViewModel : ViewModel() {

    private val listData = MutableLiveData<List<DataModel>>()

    var context: Context? = null

    fun loadData(tag: String): LiveData<List<DataModel>> {
        val dbHelper = FavoriteHelper.getInstance(context as Context)

        dbHelper.open()
        val result = dbHelper.getAll().filter { data -> data.category == tag }
        dbHelper.close()

        listData.postValue(result)

        return listData
    }

    fun remove(id: Int) {
        val dbHelper = FavoriteHelper.getInstance(context as Context)

        dbHelper.open()
        dbHelper.delete(id.toString())
        dbHelper.close()
    }
}