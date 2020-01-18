package com.github.hattamaulana.moviecatalogue.api

import android.content.Context
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.helper.GenreHelper
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.model.GenreModel
import org.json.JSONArray
import org.json.JSONObject

object MovieDbFactory {

    fun listData(array: JSONArray, tag: String, ctx: Context): ArrayList<DataModel> {
        val list = ArrayList<DataModel>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val data = refactor(tag, obj)

            data.genres = genre(tag, obj, ctx)
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

    private fun genre(tag: String, json: JSONObject, ctx: Context): String{
        val dbHelper = GenreHelper(ctx)
        val data = json.getJSONArray("genre_ids")
        val res = ArrayList<Int>()

        for (i in 0 until data.length()) {
            res.add(data.getInt(i))
        }

        dbHelper.open()
        val genre = dbHelper.getAll()
        dbHelper.close()

        val filtered = genre.filter { item ->
            res.contains(item.id) && item.category == tag  }

        val deliver = ArrayList<String>()
        filtered.forEach {
            deliver.add(it.name as String)
        }

        return deliver.joinToString(" | ")
    }

    private fun refactor(tag: String, json: JSONObject): DataModel {
        val title = json.getString(if (tag == TYPE_MOVIE) "title" else "name")
        val id = json.getInt("id")
        val image = json.getString("poster_path")
        val overview = json.getString("overview")
        val rating = json.getDouble("vote_average")

        return DataModel(id, image, title, overview, null, rating, null)
    }
}