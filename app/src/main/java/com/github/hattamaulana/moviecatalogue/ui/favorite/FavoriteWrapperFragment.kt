package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import kotlinx.android.synthetic.main.fragment_favorite_wrapper.*

private var viewStatePosition: Int? = null

class FavoriteWrapperFragment : Fragment() {

    private val EXTRA_VIEW_POSITION = "EXTRA_VIEW_POSITION"
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
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.title = "FAVORITES"
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_language) {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                return@setOnMenuItemClickListener true
            }

            true
        }

        view_pager_favorite.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            FavoriteFragment.instance(it)
        }

        view_pager_favorite.setCurrentItem(viewStatePosition ?: 0, true)
        view_pager_favorite.addOnPageChangeListener(pageChangeListener)
        tabs.setupWithViewPager(view_pager_favorite)
    }
}