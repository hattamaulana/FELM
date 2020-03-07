package com.github.hattamaulana.moviecatalogue.ui.detail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.MainActivity
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var dataIntent: DataModel
    private lateinit var similarAdapter: SimilarContentAdapter

    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        /** Set support action bar with the title */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)
            .apply { context = this@DetailActivity }

        id = intent.getIntExtra(EXTRA_ID, -1)
        if (id == -1) {
            val category = intent.getStringExtra(EXTRA_TAG) as String
            val genreIds = intent.getIntArrayExtra(EXTRA_GENRE_IDS)

            dataIntent = intent.getParcelableExtra(EXTRA_MOVIE_DETAIL) as DataModel
            dataIntent.category = category
            dataIntent.genres = genreIds?.toCollection(ArrayList())
            setData()
        } else {
            viewModel.getDataFavorite(id) { data ->
                dataIntent = data
                setData()
            }
        }
    }

    private fun setData() {
        /** Set TextView for Title, Year of Release, and overview */
        tv_title.text = dataIntent.title
        tv_release.text = dataIntent.release
        tv_overview.text = dataIntent.overview

        /** Set Label Similar Movie or Tv Show from resource */
        val similar = applicationContext.resources.getStringArray(R.array.similar)
        lbl_relates.text = similar[if (dataIntent.category == MovieDbFactory.TYPE_MOVIE) 0 else 1]

        /** Set Image Backdrop and Poster */
        setImage(dataIntent.backdropPath, iv_backdrop)
        setImage(dataIntent.posterPath, iv_poster)

        setGenre(dataIntent.genres?.toIntArray())
        setSimilarContent(dataIntent.category as String)

        viewModel.apply {
            getSimilarContent(dataIntent.category, dataIntent.id)
            listSimilarContent.observe(this@DetailActivity, Observer { content ->
                val visibility = if (content.isEmpty()) GONE else VISIBLE
                divider_view_2.visibility = visibility
                lbl_relates.visibility = visibility
                rv_relate.visibility = visibility
                similarAdapter.update(content)
            })
        }
    }

    /** Set Image with Glide */
    private fun setImage(string: String?, image: ImageView) {
        Glide.with(this).load("$IMAGE_URI/w780/$string").into(image)
    }

    /** Set Relate Content Movie or Tv Show */
    private fun setSimilarContent(category: String) {
        val layout = LinearLayoutManager(applicationContext)
            .apply { orientation = LinearLayoutManager.HORIZONTAL }
        similarAdapter = SimilarContentAdapter().apply {
            setOnCLickListener { data ->
                val intent = Intent(this@DetailActivity,
                    DetailActivity::class.java).apply {
                    putExtra(EXTRA_TAG, category)
                    putExtra(EXTRA_GENRE_IDS, data.genres?.toIntArray())
                    putExtra(EXTRA_MOVIE_DETAIL, data)
                }

                startActivity(intent)
            }
        }

        rv_relate.layoutManager = layout
        rv_relate.adapter = similarAdapter
    }

    /** Set Genre */
    private fun setGenre(ids: IntArray?) {
        val adapter = GenreAdapter()
        val layout = LinearLayoutManager(applicationContext)
            .apply { orientation = LinearLayoutManager.HORIZONTAL }

        ids?.forEach { id ->
            viewModel.findGenreByIdAsync(id) { data ->
                adapter.add(data)
            }
        }

        rv_genre.layoutManager = layout
        rv_genre.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        /** Check have added favorite and change icon */
        viewModel.checkData(dataIntent.id as Int) { isFavorite ->
            if (! isFavorite) {
                val icon = menu?.findItem(R.id.fav_movie)
                icon?.setIcon(R.drawable.ic_favorite)
            }
        }

        return true
    }

    private fun back() {
        if (id != -1) startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        back()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> back()
            R.id.language ->  startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.fav_movie -> viewModel.checkData(dataIntent.id as Int) { isFavorite ->
                val message = if (! isFavorite) {
                    viewModel.delete(dataIntent)
                    item.setIcon(R.drawable.ic_favorite_border)
                    applicationContext.getString(R.string.failed_save_favorite)
                } else {
                    viewModel.save(dataIntent)
                    item.setIcon(R.drawable.ic_favorite)
                    applicationContext.getString(R.string.save_favorite)
                }

                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return true
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TAG = "EXTRA_TAG"
        const val EXTRA_MOVIE_DETAIL = "EXTRA_MOVIE_DETAIL"
        const val EXTRA_GENRE_IDS = "EXTRA_GENRE_IDS"
    }
}