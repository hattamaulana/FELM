package com.github.hattamaulana.felm_favorites.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.hattamaulana.felm_favorites.R
import com.github.hattamaulana.felm_favorites.data.DataModel
import com.github.hattamaulana.felm_favorites.data.Provider.IMAGE_URI
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var dataIntent: DataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        /** Set support action bar with the title */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        dataIntent = intent?.getParcelableExtra(EXTRA_MOVIE_DETAIL)!!

        /** Set TextView for Title, Year of Release, and overview */
        tv_title.text = dataIntent.title
        tv_release.text = dataIntent.release
        tv_overview.text = dataIntent.overview

        /** Set Image Backdrop and Poster */
        setImage(dataIntent.backdropPath, iv_backdrop)
        setImage(dataIntent.posterPath, iv_poster)

        val adapter = GenreAdapter()
        rv_genre.layoutManager = LinearLayoutManager(applicationContext)
            .apply { orientation = LinearLayoutManager.HORIZONTAL }
        rv_genre.adapter = adapter

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)
            .apply {
                getGenre(applicationContext, dataIntent.id as Int)
                genres.observe(this@DetailActivity, Observer { l -> adapter.update(l) })
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

    /** Set Image with Glide */
    private fun setImage(string: String?, image: ImageView) {
        Glide.with(this)
            .load("$IMAGE_URI/w780/$string")
            .into(image)
    }

    companion object {
        const val EXTRA_MOVIE_DETAIL = "EXTRA_MOVIE_DETAIL"
    }

}