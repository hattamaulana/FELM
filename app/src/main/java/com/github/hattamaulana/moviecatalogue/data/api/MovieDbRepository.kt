package com.github.hattamaulana.moviecatalogue.data.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.API_URI
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.API_KEY
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.data.model.GenreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.*

class MovieDbRepository(private val context: Context) : JSONObjectRequestListener {

    private lateinit var tag: String
    private lateinit var callback: (list: List<*>)-> Unit

    private val TAG = this.javaClass.simpleName

    /**
     * Untuk mengambil data movie dan tv show
     *
     * @param tag
     * @param callback
     */
    fun getDiscover(tag: String, callback: (list: List<DataModel>)-> Unit) {
        this.tag = tag
        @Suppress("UNCHECKED_CAST")
        this.callback = callback as (List<*>) -> Unit

        request("$API_URI/discover/$tag", mapOf("sort_by" to "popularity.desc"))
            .getAsJSONObject(this)
    }

    /**
     * Untuk mengambil data genre
     *
     * @param tag
     * @param callback
     */
    fun getGenre(tag: String, callback: (list: List<GenreModel>)-> Unit) {
        @Suppress("UNCHECKED_CAST")
        this.callback = callback as (List<*>) -> Unit

        request("$API_URI/genre/$tag/list")
            .getAsJSONObject(this)
    }

    /**
     * Fungsi ini digunakan untuk melakukan request ke web service
     * dan untuk meringkas penulisan kode supaya tidak berulang-ulang.
     */
    private fun request(
        url: String, queryParams: Map<String, String>? = null
    ) : ANRequest<out ANRequest<*>> {
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
        response?.let { res ->
            when {
                res.has("results") -> CoroutineScope(Dispatchers.Default).launch {
                    val array = res.getJSONArray("results")
                    val listData = MovieDbFactory.data(array, tag)
                    callback(listData)

                    Log.d(TAG, "onResponse: listData movie: ${listData.size}")
                }

                res.has("genres") -> CoroutineScope(Dispatchers.Default).launch {
                    val array = res.getJSONArray("genres")
                    val listData = MovieDbFactory.genre(array)
                    callback(listData)

                    Log.d(TAG, "onResponse: listData genre: ${listData.size}")
                }

                else -> {
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
            val message = "Please Connect Internet"
            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show()
        }
    }

}