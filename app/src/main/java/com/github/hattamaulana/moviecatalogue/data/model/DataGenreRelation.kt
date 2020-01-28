package com.github.hattamaulana.moviecatalogue.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "data_genre_relation",
    primaryKeys = ["dataId", "genreId"],
    foreignKeys = [
        ForeignKey(
            entity = DataModel::class,
            parentColumns = ["id"],
            childColumns = ["dataId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = GenreModel::class,
            parentColumns = ["id"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class DataGenreRelation (
    val dataId: Int,
    val genreId: Int
)