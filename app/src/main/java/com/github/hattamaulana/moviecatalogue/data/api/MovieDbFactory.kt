package com.github.hattamaulana.moviecatalogue.data.api

import android.util.Log
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.data.model.GenreModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

object MovieDbFactory {

    /**
     * Api ini diambil dari https://themoviedb.org
     * dengan versi api yang digunakan adalah versi 3.
     *
     * URI Format : https://api.themoviedb.org/3/path
     */
    const val API_URI = "https://api.themoviedb.org/3"

    /**
     * Untuk melakukan download image dapat menggunakan URI dibawah
     * menggunakan library download & caching image like Glide.
     *
     * URI Format : https://image.tmdb.org/t/p/<size-image>/<file-name>
     * - size-image ->
     *      - original
     *      - w185
     *      - w256
     *      - w780
     * - file-name : Nama File Image yang akan diload.
     */
    const val IMAGE_URI = "https://image.tmdb.org/t/p/"

    /**
     * Gunakan variabel constant dibawah ini
     * untuk menentukan sedang mengambil data Movie or TV Show.
     */
    const val TYPE_MOVIE = "movie"
    const val TYPE_TV = "tv"

    /**
     * Variabel constant dibawah merupakan [API_KEY]
     * yang wajib ditambahkan pada Query Parameter
     * setiap kali melakukan request.
     */
    const val API_KEY = "08de0941b7963667bad17331070237dd"

    /**
     * Constant Key Response
     */
    const val KEY_GENRE_ID = "genre_ids"
    const val KEY_TITLE = "title"
    const val KEY_NAME = "name"
    const val KEY_ID = "id"
    const val KEY_OVERVIEW = "overview"
    const val KEY_POSTER_PATH = "poster_path"
    const val KEY_RATING = "vote_average"

    private val TAG = this.javaClass.simpleName

    /**
     * Method untuk melakukan deserialisasi dari
     * JSON ke List Object Data Model.
     *
     * @param array
     * @param tag
     * @return [List]
     */
    suspend fun data(array: JSONArray, tag: String): List<DataModel> {
        val list = ArrayList<DataModel>()

        CoroutineScope(Dispatchers.Default).launch {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val data = withContext(coroutineContext) { refactorData(tag, obj) }
                val genreIds = withContext(coroutineContext) {
                    arrayListOf<Int>().apply {
                        val ids = obj.getJSONArray(KEY_GENRE_ID)
                        for (id in 0 until ids.length()) add(ids.getInt(id))
                    }
                }

                list.add(data.apply { genres = genreIds })
                Log.d(TAG, "data: category: $tag; key=$i; value=${data.title}")
            }
        }.join()

        return list
    }

    /**
     * Method untuk melakukan deserialisasi dari
     * JSON ke List Object Data Model.
     *
     * @param json
     * @return [List]
     */
    suspend fun genre(json: JSONArray): List<GenreModel> {
        val listData = ArrayList<GenreModel>()

        CoroutineScope(Dispatchers.Default).launch {
            for (i in 0 until json.length()) {
                val `object` = json.getJSONObject(i)
                val data = refactorGenre(`object`)

                listData.add(data)
                Log.d(TAG, "data: key=$i; value=${data.name}")
            }
        }.join()

        return listData
    }

    /**
     * Proses Pengambilan data value dari JSON
     * secara satu persatu by JSON Key.
     *
     * @param json :
     * @return GenreModel
     */
    private fun refactorGenre(json: JSONObject): GenreModel {
        val id = json.getInt(KEY_ID)
        val name = json.getString(KEY_NAME)

        return GenreModel(id, name)
    }

    /**
     * Proses Pengambilan data value dari JSON
     * secara satu persatu by JSON Key.
     *
     * @param tag :
     * @param json :
     * @return DataModel
     */
    private fun refactorData(tag: String, json: JSONObject): DataModel {
        val title = json.getString(if (tag == TYPE_MOVIE) KEY_TITLE else KEY_NAME)
        val id = json.getInt(KEY_ID)
        val image = json.getString(KEY_POSTER_PATH)
        val overview = json.getString(KEY_OVERVIEW)
        val rating = json.getDouble(KEY_RATING)

        return DataModel(id, image, title, overview, rating, null)
    }
}