package com.github.hattamaulana.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModel(
    var id: Int?,
    var img: String?,
    var title: String?,
    var overview: String?,
    var genres: String?,
    var rating: Double?,
    var release: String?
) : Parcelable {
    @IgnoredOnParcel
    var category: String? = null

    interface Callback {
        fun get(p0: ArrayList<DataModel>)
    }
}