package com.github.hattamaulana.favorites.data

import android.net.Uri

object Provider {

    const val TYPE_MOVIE = "movie"
    const val TYPE_TV = "tv"
    const val AUTHORITY = "com.github.hattamaulana.moviecatalogue"
    const val SCHEME = "content"
    const val IMAGE_URI = "https://image.tmdb.org/t/p/"

    val FAVORITES_CONTENT_URI: Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(DATA_TABLE_NAME)
        .build()

    val GENRE_CONTENT_URI: Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(GENRE_TABLE_NAME)
        .build()

    /** Show */
    // contentResolver?.query(uriWithId, null, null, null, null)
}