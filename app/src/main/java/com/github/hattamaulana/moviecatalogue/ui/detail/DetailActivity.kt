package com.github.hattamaulana.moviecatalogue.ui.detail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.catalogue.CatalogueFragment
import io.github.hattamaulana.moviecatalogue.MovieModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val movie = intent
            .getParcelableExtra<MovieModel>(CatalogueFragment.ARG_DATA_MOVIE)

        movie?.let {
            img_movie.setImageResource(it.img)

            txt_title.text = it.title
            txt_overview.text = it.overview
            txt_rating.text = it.rating.toString()
            txt_release.text = it.release
            txt_genre.text = it.genres
            txt_storyline.text = it.storyLine
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


}
