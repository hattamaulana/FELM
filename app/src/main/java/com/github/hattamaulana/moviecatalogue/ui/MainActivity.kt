package com.github.hattamaulana.moviecatalogue.ui

import android.content.ContentValues
import android.os.Bundle
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
import com.github.hattamaulana.moviecatalogue.ui.favorite.FavoriteWrapperFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MainActivity : AppCompatActivity() {

    private val EXTRA_STATE_FRAGMENT = "EXTRA_STATE_FRAGMENT"

    private lateinit var stateView: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Set Toolbar without shadow */
        supportActionBar?.elevation = 0f

        val appPreferences = App.SharedPref(applicationContext)
        if (appPreferences.firstRun) {
            CoroutineScope(Dispatchers.Default).launch {
                val list = listOf(MovieDbContract.TYPE_TV, MovieDbContract.TYPE_MOVIE)
                list.forEach { item -> getData(item) }
            }
        }

        if (savedInstanceState != null ) {
            stateView = savedInstanceState.getString(EXTRA_STATE_FRAGMENT) ?:
                CatalogueWrapperFragment::class.java.simpleName

            if (stateView == FavoriteWrapperFragment::class.java.simpleName) {
                showFragment(FavoriteWrapperFragment())
            } else {
                showFragment(CatalogueWrapperFragment())
            }
        } else {
            stateView = CatalogueWrapperFragment::class.java.simpleName

            showFragment(CatalogueWrapperFragment())
        }

        /** Set Bottom Navigation On Navigation Item Selected */
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            showFragment(when (item.itemId) {
                R.id.list_movies -> {
                    stateView = CatalogueWrapperFragment::class.java.simpleName
                    CatalogueWrapperFragment()
                }

                R.id.fav_movies -> {
                    stateView = FavoriteWrapperFragment::class.java.simpleName
                    FavoriteWrapperFragment()
                }

                else -> Fragment()
            })

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_STATE_FRAGMENT, stateView)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, fragment)
            .commit()
    }

    private fun getData(tag: String) = runBlocking {
        val genreHelper = GenreHelper(applicationContext)
        val appPreferences = App.SharedPref(applicationContext)
        val repo = MovieDbRepository(this@MainActivity)

        launch(IO) {
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
}
