package com.github.hattamaulana.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModel(
    var id: Int?,
    var img: String?,
    var title: String?,
    var overview: String?,
    var posterPath: String?,
    var genre: List<Int>?,
    var rating: Double?,
    var release: String?
) : Parcelable