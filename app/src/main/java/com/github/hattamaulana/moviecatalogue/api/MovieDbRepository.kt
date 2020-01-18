package com.github.hattamaulana.moviecatalogue.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.API_URI
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.Data.API_KEY
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.model.GenreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class MovieDbRepository(private val context: Context) : JSONObjectRequestListener {

    private lateinit var mFactory: DataModel.Callback
    private lateinit var mGenreCallback: GenreModel.Callback
    private lateinit var mTag: String

    private var message: String = "Please Connect Internet"

    private val TAG = this.javaClass.simpleName

    fun getDiscover(tag: String, callback: DataModel.Callback) {
        mTag = tag
        mFactory = callback

        val req = request(
            "$API_URI/discover/$tag", mapOf("sort_by" to "popularity.desc")
        )

        req.getAsJSONObject(this)
    }

    fun getGenre(
        tag: String,
        callback: GenreModel.Callback,
        msg: String = "Please Connect Internet"
    ) {
        message = msg
        mTag = tag
        mGenreCallback = callback

        val req = request(
            "$API_URI/genre/$tag/list"
        )
        req.getAsJSONObject(this)

        Log.d(TAG, "getGenre: ${req.url}")
    }

    private fun request(
        url: String,
        queryParams: Map<String, String>? = null
    ): ANRequest<out ANRequest<*>> {
        val locale = Locale.getDefault().toLanguageTag()
        val get = AndroidNetworking.get(url)

        get.setTag(TAG)
        get.setPriority(Priority.IMMEDIATE)
        get.addQueryParameter("api_key", API_KEY)
        get.addQueryParameter("language", if (locale == "in-ID") "id-ID" else locale)
        queryParams?.forEach { get.addQueryParameter(it.key, it.value) }

        return get.build()
    }

    override fun onResponse(response: JSONObject?) {
        response?.let {
            when {
                response.has("results") -> {
                    val array = it.getJSONArray("results")
                    val listData = MovieDbFactory.listData(array, mTag, context)

                    mFactory.get(listData)
                }

                response.has("genres") -> {
                    val array = it.getJSONArray("genres")
                    val listData = MovieDbFactory.genre(array)

                    mGenreCallback.save(listData)
                }
            }
        }
    }

    override fun onError(anError: ANError?) {
        val errorMessage = anError?.message
        val errorBody = anError?.errorBody
        val errorDetail = anError?.errorDetail

        anError?.printStackTrace()
        Log.d(TAG, "onError: $errorMessage")
        Log.d(TAG, "onError: $errorBody")
        Log.d(TAG, "onError: $errorDetail")

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}