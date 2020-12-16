package com.github.hattamaulana.felm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.hattamaulana.felm.data.model.DataGenreRelation
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.model.GenreModel

@Database(
    entities = [DataModel::class, GenreModel::class, DataGenreRelation::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * This function used of get
     * Data Access Object for [DataModel]
     * */
    abstract fun favoriteDao(): FavoriteDao

    /**
     * This function used of get
     * Data Access Object for [GenreModel]
     * */
    abstract fun genreDao(): GenreDao


    /**
     * This function used of get
     * Data Access Object for [GenreModel]
     * */
    abstract fun relationDataAndGenreDao(): DataGenreDao

}