package com.github.hattamaulana.moviecatalogue.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.hattamaulana.moviecatalogue.data.model.DataModel

@Dao
interface FavoriteDao {

    @Insert
    suspend fun add(arg: DataModel)

    @Query("SELECT * FROM favorites")
    fun all(): List<DataModel>

    @Query("SELECT * FROM favorites WHERE :field LIKE :search")
    fun find(field: String, search: String): List<DataModel>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun findById(id: Int): List<DataModel>

    @Delete
    suspend fun remove(vararg args: DataModel)

}