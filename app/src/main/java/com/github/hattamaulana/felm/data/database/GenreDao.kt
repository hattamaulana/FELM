package com.github.hattamaulana.felm.data.database

import android.database.sqlite.SQLiteDatabase
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.hattamaulana.felm.data.model.GenreModel

@Dao
interface GenreDao {

    @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
    fun add(arg: GenreModel)

    @Query("SELECT * FROM genres")
    suspend fun all(): List<GenreModel>

    @Query("SELECT * FROM genres WHERE id = :id")
    suspend fun findById(id: Int): List<GenreModel>

    @Delete
    fun remove(vararg args: GenreModel)

}