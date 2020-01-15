package com.github.hattamaulana.moviecatalogue.api

object MovieDbContract {
    const val API_URI = "https://api.themoviedb.org/3"

    /**
     * URI Format : https://image.tmdb.org/t/p/<size-image>/<file-name>
     *     - size-image ->
     *          - original
     *          - w185
     *          - w256
     *          - w780
     *     - file-name : Nama File Image yang akan diload.*/
    const val IMAGE_URI = "https://image.tmdb.org/t/p/"

    /**
     * Sebagai query parameter
     * untuk menampilkan discover atau detail */
    const val TYPE_MOVIE = "movie"
    const val TYPE_TV = "tv"


    internal object Data {
        const val API_KEY = "08de0941b7963667bad17331070237dd"
    }
}