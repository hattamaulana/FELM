package com.github.hattamaulana.moviecatalogue.database

import android.provider.BaseColumns

object DatabaseContract {

    object Favorites : BaseColumns {
        const val TABLE_NAME = "favorite"
        const val MOVIE_ID = "movie_id"
        const val TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTER_PATH = "poster_path"
        const val RATING = "vote_average"
        const val GENRE = "genre"
        const val CATEGORY = "category"
    }

    object Genre : BaseColumns {
        const val TABLE_NAME = "genre"
        const val GENRE_ID = "genre_ids"
        const val NAME = "name"
        const val CATEGORY = "category"
    }
}