//package com.github.hattamaulana.moviecatalogue
//
//import android.content.Context
//import androidx.test.core.app.ApplicationProvider
//import com.github.hattamaulana.moviecatalogue.data.database.AppDatabase
//import com.github.hattamaulana.moviecatalogue.data.database.AppDbProvider
//import com.github.hattamaulana.moviecatalogue.data.database.FavoriteDao
//import com.github.hattamaulana.moviecatalogue.data.database.GenreDao
//import com.github.hattamaulana.moviecatalogue.data.model.GenreModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import org.hamcrest.core.IsEqual.equalTo
//import org.junit.After
//import org.junit.Assert.assertThat
//import org.junit.Before
//import org.junit.Test
//import java.io.IOException
//
//
//class AppDbWithRoomTest {
//
//    private lateinit var database: AppDatabase
//    private lateinit var genreDao: GenreDao
//    private lateinit var favoritesDao: FavoriteDao
//
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//
//        database = AppDbProvider.getDb(context)
//        genreDao = database.genreDao()
//        favoritesDao = database.favoriteDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() {
//        database.close()
//    }
//
//    @Test
//    @Throws(IOException::class)
//    fun writeDataGenre() {
//        val genre: GenreModel = GenreModel(1, "secukupnya")
//        genreDao.add(genre)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val genres = genreDao.findById(1)
//
//            assertThat(genres, equalTo(genre))
//        }
//    }
//
////    @Test
////    @Throws(IOException::class)
////    fun writeDataFavorite() {
////
////    }
////
////    @Test
////    @Throws(IOException::class)
////    fun readDataGenre() {
////
////    }
////
////    @Test
////    @Throws(IOException::class)
////    fun readDataFavorite() {
////
////    }
////
////    @Test
////    @Throws(IOException::class)
////    fun deleteDataGenre() {
////
////    }
////
////    @Test
////    @Throws(IOException::class)
////    fun deleteDataFavorite() {
////
////    }
//}