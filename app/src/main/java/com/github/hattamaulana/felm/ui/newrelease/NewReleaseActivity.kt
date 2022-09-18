package com.github.hattamaulana.felm.ui.newrelease

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.felm.ui.MainActivity
import com.github.hattamaulana.felm.ui.detail.DetailActivity
import com.github.hattamaulana.felm.utils.PaginationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_new_release.*

@AndroidEntryPoint
class NewReleaseActivity : AppCompatActivity() {

    private lateinit var viewModel: NewReleaseViewModel

    private var page = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_release)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = NewReleaseAdapter().apply {
            handleOnClickAction = { data ->
                val intent = Intent(this@NewReleaseActivity,
                    DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_TAG, TYPE_MOVIE)
                        putExtra(DetailActivity.EXTRA_MOVIE_DETAIL, data)
                        putExtra(DetailActivity.EXTRA_GENRE_IDS, data.genres?.toIntArray())
                    }

                startActivity(intent)
            }
        }

        rv_newrelease.adapter = adapter
        rv_newrelease.layoutManager = LinearLayoutManager(this)
            .apply {
                val listener = scrollListener(this)
                rv_newrelease.addOnScrollListener(listener)
            }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(NewReleaseViewModel::class.java)
            .apply {
                init(this@NewReleaseActivity, TYPE_MOVIE)
                data.observe(this@NewReleaseActivity,
                    Observer { list -> adapter.update(list) })
            }
    }

    private fun scrollListener(linearLayoutManager: LinearLayoutManager) =
        object : PaginationListener(linearLayoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun isLastPage(): Boolean {
                if (viewModel.totalPage == null) return false
                return page == viewModel.totalPage
            }

            override fun loadMore() {
                isLoading = true
                page += 1
                viewModel.loadMore(page) { isLoading = false }
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) handleBack()
        return true;
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handleBack()
    }

    private fun handleBack() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}