package com.github.hattamaulana.moviecatalogue.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorites")
data class DataModel(
    @PrimaryKey(autoGenerate = false)
    var id: Int?,

    @ColumnInfo(name = "poster_path")
    var posterPath: String?,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "overview")
    var overview: String?,

    @ColumnInfo(name = "rating")
    var rating: Double?,

    @ColumnInfo(name = "release")
    var release: String?
) : Parcelable {

    @IgnoredOnParcel
    @ColumnInfo(name = "category")
    var category: String? = null

    @IgnoredOnParcel
    @Ignore
    var genres: List<Int>? = null
}