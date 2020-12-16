package com.github.hattamaulana.felm.data.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.hattamaulana.felm.data.model.DataModel

@Dao
interface FavoriteDao {

    @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
    suspend fun add(arg: DataModel)

    @Query("SELECT * FROM favorites")
    suspend fun getAllAsync(): List<DataModel>

    @Query("SELECT * FROM favorites")
    fun getAll(): Cursor

    @Query("SELECT * FROM favorites WHERE title LIKE :search AND category = :category")
    suspend fun findByTitle(search: String, category: String): List<DataModel>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun findByIdAsync(id: Int): List<DataModel>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun findById(id: Int): Cursor

    @Delete
    suspend fun remove(vararg args: DataModel)

}