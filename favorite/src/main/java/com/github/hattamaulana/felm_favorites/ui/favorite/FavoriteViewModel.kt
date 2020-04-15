package com.github.hattamaulana.felm_favorites.ui.favorite

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hattamaulana.felm_favorites.data.CursorHelper
import com.github.hattamaulana.felm_favorites.data.DataModel
import com.github.hattamaulana.felm_favorites.data.Provider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel : ViewModel() {

    private val TAG = this.javaClass.simpleName

    val favorites = MutableLiveData<List<DataModel>>()

    fun getData(context: Context) = with(context) {
        Log.d(TAG, "getData: *OK")
        GlobalScope.launch(Dispatchers.IO) {
            val data = withContext(Dispatchers.IO){
                val cursor = contentResolver?.query(Provider.FAVORITES_CONTENT_URI,
                    null, null, null, null)
                CursorHelper.toArrayList(cursor)
            }

            Log.d(TAG, "getData: $data")

            favorites.postValue(data)
        }
    }
}