package com.github.hattamaulana.felm.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.github.hattamaulana.android.core.common.DiffUtilCallbackItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

const val DATA_TABLE_NAME = "favorites"
const val DATA_ID = "id"
const val DATA_BACKDROP_PATH = "backdrop_path"
const val DATA_POSTER_PATH = "poster_path"
const val DATA_TITLE = "title"
const val DATA_OVERVIEW = "overview"
const val DATA_CATEGORY = "category"


@Entity(tableName = DATA_TABLE_NAME)
@Parcelize
data class DataModel(
    @PrimaryKey(autoGenerate = false) var id: Int?,
    @ColumnInfo(name = DATA_BACKDROP_PATH) var backdropPath: String?,
    @ColumnInfo(name = DATA_POSTER_PATH) var posterPath: String?,
    @ColumnInfo(name = DATA_TITLE) var title: String?,
    @ColumnInfo(name = DATA_OVERVIEW) var overview: String?,
    @ColumnInfo(name = "rating") var rating: Double?,
    @ColumnInfo(name = "release") var release: String?
) : Parcelable, DiffUtilCallbackItem {

    @IgnoredOnParcel
    @ColumnInfo(name = DATA_CATEGORY)
    var category: String? = null

    @IgnoredOnParcel
    @Ignore
    var genres: List<Int>? = null

    override fun diff(): String = "$id"
}