package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.moviecatalogue.ui.MainViewModel
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import com.github.hattamaulana.moviecatalogue.utils.singleChoiceDialog
import kotlinx.android.synthetic.main.fragment_catalogue_wrapper.*

private var viewStatePosition: Int = 0
private const val EXTRA_VIEW_POSITION = "EXTRA_VIEW_POSITION"

class CatalogueWrapperFragment : Fragment() {

    private lateinit var sortBy: Array<String>

    private val viewModel: MainViewModel by activityViewModels()

    /* On Page ChangeListener*/
    private val pageChangeListener = object :
        ViewPager.OnPageChangeListener {
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
                loadData()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_catalogue_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val title = if (viewStatePosition == 0) R.string.list_movie else R.string.list_tv
        toolbar.title = resources.getString(title)
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))

        sortBy = arrayOf(
            resources.getString(R.string.filter_popularity),
            resources.getString(R.string.filter_revenue),
            resources.getString(R.string.filter_title),
            resources.getString(R.string.filter_rating)
        )

        loadData()
        view_pager.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            CatalogueFragment.instance(it)
        }
        view_pager.setCurrentItem(viewStatePosition, true)
        view_pager.addOnPageChangeListener(pageChangeListener)
        tabs.setupWithViewPager(view_pager)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_VIEW_POSITION, viewStatePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> {
                val direction = CatalogueWrapperFragmentDirections
                    .catalogueToSearch(ARG_CATALOGUE, viewStatePosition)
                findNavController().navigate(direction)
            }

            R.id.filter -> context?.singleChoiceDialog(sortBy) { checked ->
                val tag = if (viewStatePosition == 0) TYPE_MOVIE else TYPE_TV
                if (checked != -1) {
                    viewModel.apply {
                        setSortBy(tag, checked)
                        loadCatalogue(tag, checked)
                    }
                }
            }?.apply {
                show()
            }
        }

        return true
    }

    private fun loadData() {
        val tag = if (viewStatePosition == 0) TYPE_MOVIE else TYPE_TV

        viewModel.apply {
            catalogeStatePosition = viewStatePosition
            loadCatalogue(tag, getSortBy(tag) ?: 0)
        }
    }

    companion object {
        const val ARG_CATALOGUE = "list"
    }
}