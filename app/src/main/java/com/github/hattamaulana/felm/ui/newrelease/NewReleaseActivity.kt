package com.github.hattamaulana.felm.ui.newrelease

import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.android.core.common.BaseActivity
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.felm.databinding.ActivityNewReleaseBinding
import com.github.hattamaulana.felm.ui.MainActivity
import com.github.hattamaulana.felm.ui.detail.DetailActivity
import com.github.hattamaulana.felm.utils.PaginationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewReleaseActivity : BaseActivity<ActivityNewReleaseBinding>(
    ActivityNewReleaseBinding::inflate
) {

    private val adapter = NewReleaseAdapter()
    private val viewModel by viewModels<NewReleaseViewModel>()

    private var page = 1
    private var isLoading = false

    override fun initView(binding: ActivityNewReleaseBinding) = with(binding) {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvNewrelease.adapter = adapter.apply {
            handleOnClickAction = { data ->
                val intent = Intent(
                    this@NewReleaseActivity,
                    DetailActivity::class.java
                ).apply {
                    putExtra(DetailActivity.EXTRA_TAG, TYPE_MOVIE)
                    putExtra(DetailActivity.EXTRA_MOVIE_DETAIL, data)
                    putExtra(DetailActivity.EXTRA_GENRE_IDS, data.genres?.toIntArray())
                }

                startActivity(intent)
            }
        }

        rvNewrelease.layoutManager = LinearLayoutManager(this@NewReleaseActivity).apply {
            val listener = scrollListener(this)
            rvNewrelease.addOnScrollListener(listener)
        }
    }

    override fun initData() = with(viewModel) {
        init(this@NewReleaseActivity, TYPE_MOVIE)
        data.observe(this@NewReleaseActivity) { list -> adapter.update(list) }
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