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
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.catalogue.CatalogueFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), RequestListener<Drawable> {

    private lateinit var viewModel: DetailViewModel

    private var dataIntent: DataModel? = null
    private lateinit var fromActivity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tag = intent.getStringExtra(EXTRA_TAG) as String

        fromActivity = intent.getStringExtra(EXTRA_ACTIVITY)
            ?: CatalogueFragment::class.java.name

        dataIntent = intent.getParcelableExtra(EXTRA_MOVIE_DETAIL)
        dataIntent?.category = tag

        supportActionBar?.title = dataIntent?.title
        txt_overview.text = dataIntent?.overview
        txt_genre.text = dataIntent?.genres
        Glide.with(this)
            .load("$IMAGE_URI/w780/${dataIntent?.img}")
            .addListener(this)
            .into(img_movie)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        viewModel.context = this
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_favorite_menu, menu)

        val exist = ! viewModel.isExist(dataIntent?.id as Int)

        if(exist) {
            val icon = menu?.findItem(R.id.fav_movie)
            icon?.setIcon(R.drawable.ic_favorite)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.fav_movie -> {
                val isExist = viewModel.save(dataIntent as DataModel)
                val message = if (isExist) {
                    item.setIcon(R.drawable.ic_favorite)
                    applicationContext.getString(R.string.save_favorite)
                } else {
                    viewModel.delete(dataIntent?.id as Int)
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
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        progressBar2.visibility = View.VISIBLE
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        progressBar2.visibility = View.GONE
        return false
    }

    companion object {
        const val EXTRA_ACTIVITY = "EXTRA_ACTIVITY"
        const val EXTRA_TAG = "EXTRA_TAG"
        const val EXTRA_MOVIE_DETAIL = "EXTRA_MOVIE_DETAIL"
    }
}