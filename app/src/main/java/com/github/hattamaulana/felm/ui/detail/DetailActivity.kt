package com.github.hattamaulana.felm.ui.detail

import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.hattamaulana.android.core.common.BaseActivity
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbFactory
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.felm.databinding.ActivityDetailBinding
import com.github.hattamaulana.felm.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(
    ActivityDetailBinding::inflate
) {

    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var dataIntent: DataModel
    private lateinit var similarAdapter: SimilarContentAdapter

    private var id: Int = -1

    override fun initView(binding: ActivityDetailBinding)= with(binding) {
        setSupportActionBar(findViewById(R.id.toolbar))

        /** Set support action bar with the title */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        id = intent.getIntExtra(EXTRA_ID, -1)
        if (id == -1) {
            val category = intent.getStringExtra(EXTRA_TAG) as String
            val genreIds = intent.getIntArrayExtra(EXTRA_GENRE_IDS)

            dataIntent = intent.getParcelableExtra<DataModel>(EXTRA_MOVIE_DETAIL) as DataModel
            dataIntent.category = category
            dataIntent.genres = genreIds?.toCollection(ArrayList())
        } else {
            viewModel.getDataFavorite(id) { data -> dataIntent = data }
        }

        /** Set TextView for Title, Year of Release, and overview */
        tvTitle.text = dataIntent.title
        tvRelease.text = dataIntent.release
        tvOverview.text = dataIntent.overview

        /** Set Label Similar Movie or Tv Show from resource */
        val similar = applicationContext.resources.getStringArray(R.array.similar)
        lblRelates.text = similar[if (dataIntent.category == MovieDbFactory.TYPE_MOVIE) 0 else 1]

        /** Set Image Backdrop and Poster */
        setImage(dataIntent.backdropPath, ivBackdrop)
        setImage(dataIntent.posterPath, ivPoster)

        setGenre(dataIntent.genres?.toIntArray())
        setSimilarContent(dataIntent.category as String)
    }

    override fun initData() = with(viewModel) {
        getSimilarContent(dataIntent.category, dataIntent.id)
        listSimilarContent.observe(this@DetailActivity, Observer { content ->
            val visibility = if (content.isEmpty()) GONE else VISIBLE
            binding?.dividerView2?.visibility = visibility
            binding?.lblRelates?.visibility = visibility
            binding?.rvRelate?.visibility = visibility

            similarAdapter.update(content)
        })
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

        binding?.rvRelate?.layoutManager = layout
        binding?.rvRelate?.adapter = similarAdapter
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

        binding?.rvGenre?.layoutManager = layout
        binding?.rvGenre?.adapter = adapter
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