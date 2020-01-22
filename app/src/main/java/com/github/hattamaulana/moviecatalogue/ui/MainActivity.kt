package com.github.hattamaulana.moviecatalogue.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.hattamaulana.moviecatalogue.App
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract
import com.github.hattamaulana.moviecatalogue.api.MovieDbRepository
import com.github.hattamaulana.moviecatalogue.database.DatabaseContract
import com.github.hattamaulana.moviecatalogue.helper.GenreHelper
import com.github.hattamaulana.moviecatalogue.model.GenreModel
import com.github.hattamaulana.moviecatalogue.ui.catalogue.CatalogueWrapperFragment
import com.github.hattamaulana.moviecatalogue.ui.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.name

    private suspend fun getData(tag: String) {
        val genreHelper = GenreHelper(applicationContext)
        val appPreferences = App.SharedPref(applicationContext)
        val repo = MovieDbRepository(this@MainActivity)

        withContext(CoroutineScope(IO).coroutineContext) {
            repo.getGenre(tag, object : GenreModel.Callback {
                override fun save(p0: List<GenreModel>) {
                    p0.forEach { item ->
                        val value = ContentValues()
                        value.put(DatabaseContract.Genre.GENRE_ID, item.id)
                        value.put(DatabaseContract.Genre.NAME, item.name)
                        value.put(DatabaseContract.Genre.CATEGORY, tag)

                        genreHelper.open()
                        genreHelper.insert(value)
                        genreHelper.close()
                        appPreferences.firstRun = false
                    }
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appPreferences = App.SharedPref(applicationContext)
        if (appPreferences.firstRun) {
            CoroutineScope(Dispatchers.Default).launch {
                val list = listOf(MovieDbContract.TYPE_TV, MovieDbContract.TYPE_MOVIE)
                list.forEach { item -> getData(item) }
            }
        }

        Log.d(TAG, "onCreate=${appPreferences.firstRun}")
        supportActionBar?.elevation = 0f

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            showFragment(when (item.itemId) {
                R.id.list_movies -> CatalogueWrapperFragment()
                R.id.fav_movies -> FavoriteFragment()
                else -> Fragment()
            })

            true
        }

        /** Menampilkan Fragment Default*/
        showFragment(CatalogueWrapperFragment())
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, fragment)
            .commit()
    }
}
