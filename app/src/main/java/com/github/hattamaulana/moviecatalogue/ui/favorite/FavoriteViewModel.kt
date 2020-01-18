package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.helper.FavoriteHelper
import com.github.hattamaulana.moviecatalogue.model.DataModel

class FavoriteViewModel : ViewModel() {

    private val listData = MutableLiveData<ArrayList<DataModel>>()

    fun loadData(context: Context): LiveData<ArrayList<DataModel>> {
        val dbHelper = FavoriteHelper.getInstance(context)
        dbHelper.open()

        val result = dbHelper.getAll()

        dbHelper.close()
        listData.postValue(result)

        return listData
    }

    fun remove(id: Int, context: Context) {
        val dbHelper = FavoriteHelper.getInstance(context)

        dbHelper.open()
        dbHelper.delete(id.toString())
        dbHelper.close()
    }
}