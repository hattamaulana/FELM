package com.github.hattamaulana.felm_favorites.ui.favorite

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.felm_favorites.R
import com.github.hattamaulana.felm_favorites.data.Provider
import com.github.hattamaulana.felm_favorites.data.Provider.TYPE_MOVIE
import com.github.hattamaulana.felm_favorites.data.Provider.TYPE_TV
import com.github.hattamaulana.felm_favorites.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private val adapter = FavoriteAdapter()

    private lateinit var viewModel: FavoriteViewModel

    class PagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment =
            instance(position)

        override fun getCount(): Int =
            TAB_TITLES.size

        override fun getPageTitle(position: Int): CharSequence? =
            context.resources.getString(TAB_TITLES[position])

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val arg = arguments?.getInt(EXTRA_TYPE_FAVORITE, 0)
        val type = if (arg == 0) TYPE_MOVIE else TYPE_TV

        rv_favorite.layoutManager = LinearLayoutManager(context)
        rv_favorite.adapter = adapter.apply {
            handleClickAction = { data ->
                val intent = Intent(context, DetailActivity::class.java)
                    .apply { putExtra(DetailActivity.EXTRA_MOVIE_DETAIL, data) }
                startActivity(intent)
            }
        }

        try {
            context?.favoriteContentObserver()

            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FavoriteViewModel::class.java)
                .apply {
                    getData(context as Context)
                    favorites.observe(this@FavoriteFragment, Observer { list ->
                        val filtered = list.filter { it.category == type }
                        adapter.update(filtered)
                    })
                }

        } catch (e: SecurityException) {
            Log.e(this::class.java.simpleName, "ERROR")
        }
    }

    private fun Context.favoriteContentObserver () {
        val handlerThread = HandlerThread("DataObserver").apply { start() }
        val handler = Handler(handlerThread.looper)
        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                with (this@favoriteContentObserver) {
                    viewModel.getData(this)
                }
            }
        }

        contentResolver?.registerContentObserver(Provider.FAVORITES_CONTENT_URI,
            true, observer)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_movie, R.string.tab_tv_series)

        const val EXTRA_TYPE_FAVORITE = "EXTRA_TYPE_FAVORITE"

        fun instance(index: Int): FavoriteFragment =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_TYPE_FAVORITE, index)
                }
            }
    }

}