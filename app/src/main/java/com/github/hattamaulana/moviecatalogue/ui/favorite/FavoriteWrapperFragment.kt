package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import kotlinx.android.synthetic.main.fragment_catalogue_wrapper.*
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.*
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.tabs
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.toolbar

private var viewStatePosition: Int? = null

class FavoriteWrapperFragment : Fragment() {

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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_favorite_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewState = savedInstanceState?.getInt(EXTRA_VIEW_POSITION) ?: -1
        if (viewState != -1) {
            viewStatePosition = viewState
        }

        /** Setup Toolbar */
        val title = if (viewStatePosition ?: 0 == 0) R.string.favorite_movie else R.string.favorite_tv
        toolbar.title = resources.getString(title)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener(onMenuItemSelected())

        view_pager_favorite.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            FavoriteFragment.instance(it)
        }

        view_pager_favorite.setCurrentItem(viewStatePosition ?: 0, true)
        view_pager_favorite.addOnPageChangeListener(pageChangeListener)
        tabs.setupWithViewPager(view_pager_favorite)
    }

    /** Callback Menu Item Selected */
    private fun onMenuItemSelected() = Toolbar.OnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.search -> FavoriteWrapperFragmentDirections
                .favoriteToFragment(ARG_FAVORITES, viewStatePosition ?: 0)
            R.id.iv_search -> null
            else -> null
        }?.let {
            findNavController().navigate(it)
        }

        return@OnMenuItemClickListener true
    }

    companion object {
        const val ARG_FAVORITES = "favorites"

        private val EXTRA_VIEW_POSITION = "EXTRA_VIEW_POSITION"
    }
}