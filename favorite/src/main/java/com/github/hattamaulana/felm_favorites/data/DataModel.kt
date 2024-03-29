package com.github.hattamaulana.felm_favorites.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val DATA_TABLE_NAME = "felm.favorites"
const val DATA_ID = "id"
const val DATA_BACKDROP_PATH = "backdrop_path"
const val DATA_POSTER_PATH = "poster_path"
const val DATA_TITLE = "title"
const val DATA_OVERVIEW = "overview"
const val DATA_CATEGORY = "category"
const val DATA_RATING = "rating"
const val DATA_RELEASE = "release"

@Parcelize
data class DataModel(
    var id: Int?,
    var title: String?,
    var backdropPath: String?,
    var posterPath: String?,
    var overview: String?,
    var category: String?,
    var rating: Double?,
    var release: String?
) : Parcelable