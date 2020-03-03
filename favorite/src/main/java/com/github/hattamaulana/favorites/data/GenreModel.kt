package com.github.hattamaulana.favorites.data

const val GENRE_TABLE_NAME = "genres"
const val GENRE_ID = "id"
const val GENRE_NAME = "name"

data class GenreModel(
    var id: Int? = null,
    var name: String? = null
)