package com.github.hattamaulana.moviecatalogue.ui.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.ArrayList

class DetailActivity : AppCompatActivity(), RequestListener<Drawable> {

    private lateinit var viewModel: DetailViewModel
    private lateinit var dataIntent: DataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        val tag = intent.getStringExtra(EXTRA_TAG) as String
        val genreIds = intent.getIntArrayExtra(EXTRA_GENRE_IDS)

        dataIntent = intent.getParcelableExtra(EXTRA_MOVIE_DETAIL) as DataModel
        dataIntent.category = tag
        dataIntent.genres = genreIds?.toCollection(ArrayList())

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)
        viewModel.context = this

        /** Set support action bar with the title */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = dataIntent.title

        /** Set Content View */
        txt_overview.text = dataIntent.overview
        Glide.with(this)
            .load("$IMAGE_URI/w780/${dataIntent.backdropPath}")
            .addListener(this)
            .into(backdrop_movie)

        /** Set Genre */
        setGenre(genreIds)
    }

    private fun setGenre(ids: IntArray?) {
        ids?.forEach { id ->
            viewModel.findGenreByIdAsync(id) {
                // TODO : update view at here
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_favorite_menu, menu)

        /** Check have added favorite and change icon */
        viewModel.checkData(dataIntent.id as Int) {
            if (!it) {
                val icon = menu?.findItem(R.id.fav_movie)
                icon?.setIcon(R.drawable.ic_favorite)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.fav_movie -> viewModel.checkData(dataIntent.id as Int) {
                val message = if (it) {
                    viewModel.save(dataIntent)
                    item.setIcon(R.drawable.ic_favorite)
                    applicationContext.getString(R.string.save_favorite)
                } else {
                    viewModel.delete(dataIntent)
                    item.setIcon(R.drawable.ic_favorite_border)
                    applicationContext.getString(R.string.failed_save_favorite)
                }

                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return true
    }

    override fun onLoadFailed(
        e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
    ): Boolean {
        progressBar2.visibility = View.VISIBLE
        return false
    }

    override fun onResourceReady(
        resource: Drawable?, model: Any?, target: Target<Drawable>?,
        dataSource: DataSource?, isFirstResource: Boolean
    ): Boolean {
        progressBar2.visibility = View.GONE
        return false
    }

    companion object {
        const val EXTRA_TAG = "EXTRA_TAG"
        const val EXTRA_MOVIE_DETAIL = "EXTRA_MOVIE_DETAIL"
        const val EXTRA_GENRE_IDS = "EXTRA_GENRE_IDS"
    }
}