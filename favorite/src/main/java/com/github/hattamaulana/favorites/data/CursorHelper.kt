package com.github.hattamaulana.favorites.data

import android.database.Cursor

object CursorHelper {

    fun toArrayList(cursor: Cursor?): ArrayList<DataModel> {
        val notesList = ArrayList<DataModel>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DATA_ID))
                val title = getString(getColumnIndexOrThrow(DATA_TITLE))
                val backdropPath = getString(getColumnIndexOrThrow(DATA_BACKDROP_PATH))
                val posterPath = getString(getColumnIndexOrThrow(DATA_POSTER_PATH))
                val overview = getString(getColumnIndexOrThrow(DATA_OVERVIEW))
                val category = getString(getColumnIndexOrThrow(DATA_CATEGORY))
                val rating = getDouble(getColumnIndexOrThrow(DATA_RATING))
                val release = getString(getColumnIndexOrThrow(DATA_RELEASE))

                val data = DataModel(id, title, backdropPath, posterPath, overview, category,
                    rating, release)
                notesList.add(data)
            }
        }

        return notesList
    }

    fun toObject(cursor: Cursor?): DataModel {
        var data: DataModel? = null
        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DATA_ID))
            val title = getString(getColumnIndexOrThrow(DATA_TITLE))
            val backdropPath = getString(getColumnIndexOrThrow(DATA_BACKDROP_PATH))
            val posterPath = getString(getColumnIndexOrThrow(DATA_POSTER_PATH))
            val overview = getString(getColumnIndexOrThrow(DATA_OVERVIEW))
            val category = getString(getColumnIndexOrThrow(DATA_CATEGORY))
            val rating = getDouble(getColumnIndexOrThrow(DATA_RATING))
            val release = getString(getColumnIndexOrThrow(DATA_RELEASE))

            data = DataModel(id, title, backdropPath, posterPath, overview, category, rating,
                release)
        }

        return data as DataModel
    }

    fun toGenre(cursor: Cursor?): List<GenreModel> {
        val list = ArrayList<GenreModel>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(GENRE_ID))
                val name = getString(getColumnIndexOrThrow(GENRE_NAME))
                list.add(GenreModel(id, name))
            }
        }

        return list
    }

}