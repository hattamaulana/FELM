package com.github.hattamaulana.felm.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.hattamaulana.android.core.common.BaseActivity
import com.github.hattamaulana.felm.App
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.felm.data.remote.MovieDbRepository
import com.github.hattamaulana.felm.data.local.DatabaseHelper
import com.github.hattamaulana.felm.databinding.ActivityMainBinding
import com.github.hattamaulana.felm.receiver.ReminderReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {

    private lateinit var appPreferences: App.SharedPref

    override fun initView(binding: ActivityMainBinding) = with(binding) {
        /** Set Toolbar without shadow */
        supportActionBar?.elevation = 0f

        /** Set First Launch Apps Configuration */
        appPreferences = App.SharedPref(applicationContext)
        if (appPreferences.firstRun)  {
            val reminderReceiver = ReminderReceiver()
            reminderReceiver.setDailyMorning(applicationContext)
            reminderReceiver.setNewRelease(applicationContext)
            listOf(TYPE_TV, TYPE_MOVIE).forEach { type -> storeGenre(type) }
        }


        /** Set Bottom Navigation On Navigation Item Selected */
        val navController = findNavController(R.id.home_fragment)
        bottomNavigation.setupWithNavController(navController)
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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