package com.github.hattamaulana.moviecatalogue.model

data class GenreModel(
    var id: Int? = null,
    var name: String? = null
) {
    interface Callback {
        fun save(p0: List<GenreModel>)
    }
}