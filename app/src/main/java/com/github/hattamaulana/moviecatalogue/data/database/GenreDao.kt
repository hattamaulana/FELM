package com.github.hattamaulana.moviecatalogue.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.hattamaulana.moviecatalogue.data.model.GenreModel

@Dao
interface GenreDao {

    @Insert
    fun add(arg: GenreModel)

    @Query("SELECT * FROM genres")
    suspend fun all(): List<GenreModel>

    @Query("SELECT * FROM genres WHERE id = :id")
    suspend fun findById(id: Int): List<GenreModel>

    @Delete
    fun remove(vararg args: GenreModel)

}