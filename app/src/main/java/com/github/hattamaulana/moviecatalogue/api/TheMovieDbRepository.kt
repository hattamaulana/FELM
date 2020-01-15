package com.github.hattamaulana.moviecatalogue.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.model.GenreModel
import org.json.JSONObject
import java.util.*

class TheMovieDbRepository(private val context: Context) : JSONObjectRequestListener {

    private val TAG = this.javaClass.simpleName
    private val URL = "https://api.themoviedb.org/3"
    private val API_KEY = "08de0941b7963667bad17331070237dd"

    private lateinit var mFactory: TheMovieDbFactory.Callback
    private lateinit var mGenreCallback: GenreModel.Callback
    private lateinit var mTag: String

    fun getDiscover(tag: String, callback: TheMovieDbFactory.Callback) {
        mTag = tag
        mFactory = callback

        val req = request("$URL/discover/$tag", mapOf("sort_by" to "popularity.desc"))
        req.getAsJSONObject(this)
    }

    fun getGenre(tag: String, callback: GenreModel.Callback) {
        mTag = tag
        mGenreCallback = callback

        val req = request("$URL/genre/$tag/list")
        req.getAsJSONObject(this)

        Log.d(TAG, "getGenre: ${req.url}")
    }

    private fun request(
        url: String = URL,
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
            val factory = TheMovieDbFactory()

            when {
                it.has("results") -> {
                    val array = it.getJSONArray("results")
                    val listData = factory.listData(array, mTag)

                    mFactory.getData(listData)
                }

                it.has("genres") -> {
                    val array = it.getJSONArray("genres")
                    val listData = factory.genre(array)

                    mGenreCallback.save(listData)
                }
            }
        }
    }

    override fun onError(anError: ANError?) {
        val message = anError?.message
        val errorBody = anError?.errorBody
        val errorDetail = anError?.errorDetail

        anError?.printStackTrace()
        Log.d(TAG, "onError: $message")
        Log.d(TAG, "onError: $errorBody")
        Log.d(TAG, "onError: $errorDetail")

        Toast.makeText(context, R.string.warning_error_load, Toast.LENGTH_SHORT)
            .show()
    }
}