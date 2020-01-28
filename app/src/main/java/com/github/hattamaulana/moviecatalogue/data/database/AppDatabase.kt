package com.github.hattamaulana.moviecatalogue.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.hattamaulana.moviecatalogue.data.model.DataGenreRelation
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.data.model.GenreModel

@Database(
    entities = [DataModel::class, GenreModel::class, DataGenreRelation::class],
    version = 1,
    exportSchema = false)
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