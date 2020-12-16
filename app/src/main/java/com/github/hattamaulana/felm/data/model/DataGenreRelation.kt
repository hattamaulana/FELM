package com.github.hattamaulana.felm.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

const val RELATION_TABLE_NAME = "data_genre_relation"
const val RELATION_DATA_ID = "dataId"
const val RELATION_GENRE_ID = "genreId"

@Entity(
    tableName = RELATION_TABLE_NAME,
    primaryKeys = [RELATION_DATA_ID, RELATION_GENRE_ID],
    foreignKeys = [
        ForeignKey(
            entity = DataModel::class,
            parentColumns = [DATA_ID],
            childColumns = [RELATION_DATA_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreModel::class,
            parentColumns = [GENRE_ID],
            childColumns = [RELATION_GENRE_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DataGenreRelation(
    val dataId: Int,
    val genreId: Int
)