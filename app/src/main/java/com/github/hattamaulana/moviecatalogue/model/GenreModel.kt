package com.github.hattamaulana.moviecatalogue.model

data class GenreModel(
    var id: Int? = null,
    var name: String? = null
) {
    var listGenreModel: List<GenreModel> = ArrayList()

    interface Callback {
        fun save(p0: List<GenreModel>)
    }
}