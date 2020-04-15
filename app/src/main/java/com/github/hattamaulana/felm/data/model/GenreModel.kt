package com.github.hattamaulana.felm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val GENRE_TABLE_NAME = "genres"
const val GENRE_ID = "id"
const val GENRE_NAME = "name"
const val GENRE_CATEGORY = "category"

@Entity(tableName = GENRE_TABLE_NAME)
data class GenreModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = GENRE_ID)
    var id: Int? = null,

    @ColumnInfo(name = GENRE_NAME)
    var name: String? = null
) {
    @ColumnInfo(name = GENRE_CATEGORY)
    var category: String? = null
}