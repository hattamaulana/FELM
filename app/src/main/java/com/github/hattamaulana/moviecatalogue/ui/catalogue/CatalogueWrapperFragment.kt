package com.github.hattamaulana.moviecatalogue.ui.catalogue

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

private var viewStatePosition: Int? = null
private const val EXTRA_VIEW_POSITION = "EXTRA_VIEW_POSITION"

class CatalogueWrapperFragment : Fragment() {

    /* On Page ChangeListener*/
    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {}

        override fun onPageSelected(position: Int) {
            viewStatePosition = position
            val title = if (position == 0) R.string.list_movie else R.string.list_tv
            toolbar.title = resources.getString(title)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_catalogue_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewState = savedInstanceState?.getInt(EXTRA_VIEW_POSITION) ?: -1
        if (viewState != -1) {
            viewStatePosition = viewState
        }

        /* Set Toolbar */
        val title = if (viewStatePosition ?: 0 == 0) R.string.list_movie else R.string.list_tv
        toolbar.title = resources.getString(title)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener(onMenuItemSelected())

        view_pager.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            CatalogueFragment.instance(it)
        }

        view_pager.setCurrentItem(viewStatePosition ?: 0, true)
        view_pager.addOnPageChangeListener(pageChangeListener)
        tabs.setupWithViewPager(view_pager)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_VIEW_POSITION, viewStatePosition ?: 0)
    }

    /** Callback Menu Item Selected */
    private fun onMenuItemSelected() = Toolbar.OnMenuItemClickListener { item ->
        when(item.itemId) {
            R.id.search -> CatalogueWrapperFragmentDirections
                .catalogueToSearch(ARG_CATALOGUE, viewStatePosition ?: 0)
            R.id.iv_search -> null
            else -> null
        }?.let {
            findNavController().navigate(it)
        }

        return@OnMenuItemClickListener true
    }

    companion object {
        const val ARG_CATALOGUE = "list"
    }
}