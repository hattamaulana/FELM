package com.github.hattamaulana.moviecatalogue.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.hattamaulana.moviecatalogue.App
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.data.database.DatabaseHelper
import com.github.hattamaulana.moviecatalogue.ui.newrelease.NewReleaseActivity
import com.github.hattamaulana.moviecatalogue.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appPreferences: App.SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Set Toolbar without shadow */
        supportActionBar?.elevation = 0f

        /** Set First Launch Apps Configuration */
        appPreferences = App.SharedPref(applicationContext)
        if (appPreferences.firstRun)  {
            listOf(TYPE_TV, TYPE_MOVIE).forEach { type -> storeGenre(type) }
        }

        val title = R.array.title_notification
        val message = R.array.message_notification
        sendNotification(0, title, message, Intent(this, NewReleaseActivity::class.java))

        /** Set Bottom Navigation On Navigation Item Selected */
        val navController = findNavController(R.id.home_fragment)
        bottom_navigation.setupWithNavController(navController)
    }

    /**
     * Digunakan untuk melakukan penyimpanan data genre movie dan tv show.
     * Dimana akan diload saat aplikasi pertama kali dijalankan.
     *
     * @param tag: String TV_SHOW OR MOVIE
     */
    private fun storeGenre(tag: String) {
        val TAG = this.javaClass.simpleName
        val repo = MovieDbRepository(this@MainActivity)
        val dbProvider = DatabaseHelper.openDb(this@MainActivity)
        val dao = dbProvider.genreDao()

        repo.getGenre(tag) { list ->
            list.forEach { genre ->
                try {
                    dao.add(genre)
                } catch (e: Exception) {
                    Log.d(TAG, "storeGenre: $genre ")
                    Log.d(TAG, "storeGenre: Was Add ")
                }
            }

            appPreferences.firstRun = false
        }
    }

}