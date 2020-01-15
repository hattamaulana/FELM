package com.github.hattamaulana.moviecatalogue.api

import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.model.GenreModel
import org.json.JSONArray
import org.json.JSONObject

object MovieDbFactory {

    fun listData(array: JSONArray, tag: String): ArrayList<DataModel> {
        val list = ArrayList<DataModel>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val data = refactor(tag, obj)

            data.genre = genre(obj)
            list.add(data)
        }

        return list
    }

    fun genre(json: JSONArray): List<GenreModel> {
        val listData = ArrayList<GenreModel>()

        for (i in 0 until json.length()) {
            val id = json.getJSONObject(i).getInt("id")
            val name = json.getJSONObject(i).getString("name")

            listData.add(GenreModel(id, name))
        }

        return listData
    }

    fun genre(json: JSONObject): List<Int> {
        val data = json.getJSONArray("genre_ids")
        val res = ArrayList<Int>()

        for (i in 0 until data.length()) {
            res.add(data.getInt(i))
        }

        return res
    }

    private fun refactor(tag: String, json: JSONObject): DataModel {
        val title = json.getString(if (tag == TYPE_MOVIE) "title" else "name")
        val id = json.getInt("id")
        val image = json.getString("poster_path")
        val overview = json.getString("overview")
        val posterPath = json.getString("poster_path")
        val rating = json.getDouble("vote_average")

        return DataModel(id, image, title, overview, posterPath, null, rating, null)
    }
}