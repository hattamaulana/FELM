package com.github.hattamaulana.moviecatalogue.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.model.DataModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), RequestListener<Drawable> {

    companion object {
        const val EXTRA_TAG = "EXTRA_TAG"
        const val EXTRA_MOVIE_DETAIL = "EXTRA_MOVIE_DETAIL"
    }

    private lateinit var mViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tag = intent.getStringExtra(EXTRA_TAG)
        val data = intent.getParcelableExtra<DataModel>(EXTRA_MOVIE_DETAIL)

        supportActionBar?.title = data?.title
        txt_overview.text = data?.overview
        Glide.with(this)
            .load("$IMAGE_URI/w780/${data?.img}")
            .addListener(this)
            .into(img_movie)

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        mViewModel.context = this
        mViewModel.getGenre(tag, data?.genre ?: ArrayList())
            .observe(this, Observer { list ->
                txt_genre.text = list.joinToString(" ")
            })
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
}
