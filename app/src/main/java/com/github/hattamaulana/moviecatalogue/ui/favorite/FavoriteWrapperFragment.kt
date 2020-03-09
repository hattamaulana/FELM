package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.moviecatalogue.ui.MainViewModel
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.*

private var viewStatePosition: Int = 0

class FavoriteWrapperFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            viewStatePosition = position
            val title = if (position == 0) R.string.favorite_movie else R.string.favorite_tv
            toolbar.title = resources.getString(title)
            viewModel.loadFavorites(tag(viewStatePosition))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_favorite_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = if (viewStatePosition == 0) R.string.favorite_movie else R.string.favorite_tv
        toolbar.title = resources.getString(title)

        viewStatePosition = savedInstanceState?.getInt(EXTRA_VIEW_POSITION) ?: 0

        viewModel.loadFavorites(tag(viewStatePosition))
        view_pager_favorite.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            FavoriteFragment.instance(it)
        }
        view_pager_favorite.setCurrentItem(viewStatePosition, true)
        view_pager_favorite.addOnPageChangeListener(pageChangeListener)
        tabs.setupWithViewPager(view_pager_favorite)
    }

    private fun tag(arg: Int): String = if (arg == 0) TYPE_MOVIE else TYPE_TV

    companion object {
        private val EXTRA_VIEW_POSITION = "EXTRA_VIEW_POSITION"
    }
}