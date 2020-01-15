package com.github.hattamaulana.moviecatalogue.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.moviecatalogue.api.TheMovieDbRepository
import com.github.hattamaulana.moviecatalogue.model.GenreModel

class DetailViewModel : ViewModel() {

    var context: Context? = null

    private val TAG = this.javaClass.simpleName
    private val mData = MutableLiveData<List<String>>()

    fun getGenre(tag: String, gId: List<Int>): LiveData<List<String>> {
        val repo = TheMovieDbRepository(context!!)

        repo.getGenre(tag, object : GenreModel.Callback {
            override fun save(p0: List<GenreModel>) {
                val tmp = ArrayList<String>()

                gId.forEach { i ->
                    p0.forEach { j ->
                        if (i == j.id) tmp.add(j.name ?: "")
                    }
                }

                mData.postValue(tmp)
            }
        })

        return mData
    }
}