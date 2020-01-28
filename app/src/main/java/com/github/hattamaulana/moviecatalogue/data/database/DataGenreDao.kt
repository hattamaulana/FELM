package com.github.hattamaulana.moviecatalogue.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.hattamaulana.moviecatalogue.data.model.DataGenreRelation

@Dao
interface DataGenreDao {

    @Insert
    suspend fun add(arg: DataGenreRelation)

    @Query("SELECT * FROM data_genre_relation WHERE dataId = :id")
    suspend fun find(id: Int): List<DataGenreRelation>

    @Delete
    suspend fun remove(vararg arg: DataGenreRelation)

}