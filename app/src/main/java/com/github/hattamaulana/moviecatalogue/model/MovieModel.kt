package io.github.hattamaulana.moviecatalogue

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieModel(
    var title: String?,
    var img: Int,
    var release: Int?,
    var rating: Double?,
    var overview: String?,
    var storyLine: String?,
    var genres: String?
) : Parcelable