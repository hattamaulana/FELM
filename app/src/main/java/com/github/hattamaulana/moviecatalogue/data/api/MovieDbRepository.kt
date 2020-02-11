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
    private lateinit var discoverCallback: (list: List<*>, totalPage: Int)-> Unit
    private lateinit var genreCallback: (list: List<*>)-> Unit

    private val TAG = this.javaClass.simpleName

    /** Untuk melakukan filtering data yang ditampilkan */
    fun filter(map: Map<String, String>) {
        request("", map)
    }

    /**
     * Untuk melakukan searching data ke web service, dengan parameter :
     * @param tag : Menampung nilai movies atau tv show
     */
    fun search(
        tag: String, query: String, callback: (list: List<DataModel>, totalPage: Int) -> Unit
    ) {
        this.tag = tag
        @Suppress("UNCHECKED_CAST")
        discoverCallback = callback as (List<*>, Int) -> Unit

        request("$API_URI/search/$tag/", mapOf("query" to query))
    }

    /**
     * Untuk mengambil data movie dan tv show
     *
     * @param tag
     * @param callback
     */
    fun getDiscover(
        tag: String, page: Int, callback: (list: List<DataModel>, totalPage: Int)-> Unit
    ) {
        this.tag = tag
        @Suppress("UNCHECKED_CAST")
        discoverCallback = callback as (List<*>, Int) -> Unit

        val query = mapOf(
            "sort_by" to "popularity.desc",
            "page" to "$page"
        )

        request("$API_URI/discover/$tag", query)
    }

    /**
     * Untuk mengambil data genre
     *
     * @param tag
     * @param callback
     */
    fun getGenre(tag: String, callback: (list: List<GenreModel>) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        genreCallback = callback as (List<*>) -> Unit

        request("$API_URI/genre/$tag/list")
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

        return get.build().apply {
            Log.d(TAG, "search: $requestBody")
            Log.d(TAG, "search: $url")

            getAsJSONObject(this@MovieDbRepository)
        }
    }

    override fun onResponse(response: JSONObject?) {
        response?.let { res ->
            when {
                res.has("results") -> CoroutineScope(Dispatchers.Default).launch {
                    val totalPage = res.getInt("total_pages")
                    val array = res.getJSONArray("results")
                    val listData = MovieDbFactory.data(array, tag)

                    Log.d(TAG, "onResponse: totalPage: $totalPage")
                    Log.d(TAG, "onResponse: listData: ${listData.size}")

                    discoverCallback(listData, totalPage)
                }

                res.has("genres") -> CoroutineScope(Dispatchers.Default).launch {
                    val array = res.getJSONArray("genres")
                    val listData = MovieDbFactory.genre(array)

                    genreCallback(listData)
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