package com.github.hattamaulana.felm_favorites.ui.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.felm_favorites.data.CursorHelper
import com.github.hattamaulana.felm_favorites.data.GenreModel
import com.github.hattamaulana.felm_favorites.data.Provider.GENRE_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {

    val genres = MutableLiveData<List<GenreModel>>()

    fun getGenre(context: Context, id: Int) = with(context) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = withContext(Dispatchers.IO){
                val cursor = contentResolver?.query(
                    Uri.parse("$GENRE_CONTENT_URI/$id"),
                    null, null, null, null)
                CursorHelper.toGenre(cursor)
            }

            genres.postValue(data)
        }
    }
}