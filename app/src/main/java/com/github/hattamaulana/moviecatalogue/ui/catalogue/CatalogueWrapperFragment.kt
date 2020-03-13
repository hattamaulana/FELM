package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.ui.MainViewModel
import com.github.hattamaulana.moviecatalogue.ui.TabLayoutAdapter
import com.github.hattamaulana.moviecatalogue.utils.TabChangeListener
import com.github.hattamaulana.moviecatalogue.utils.singleChoiceDialog
import kotlinx.android.synthetic.main.fragment_catalogue_wrapper.*

private var state: Int = 0

class CatalogueWrapperFragment : Fragment() {

    private lateinit var category: Array<String>
    private lateinit var sortBy: Array<String>

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_catalogue_wrapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setTitle()
        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))

        category = resources.getStringArray(R.array.category)
        sortBy = resources.getStringArray(R.array.filtering_data)

        loadData()
        view_pager.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            CatalogueFragment.instance(it)
        }

        view_pager.setCurrentItem(state, true)
        view_pager.addOnPageChangeListener(TabChangeListener { position ->
            state = position
            setTitle()
            loadData()
        })

        tabs.setupWithViewPager(view_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> {
                val direction = CatalogueWrapperFragmentDirections
                    .catalogueToSearch(ARG_CATALOGUE, state)
                findNavController().navigate(direction)
            }

            R.id.filter -> context?.singleChoiceDialog("Filter Berdasarkan :", sortBy) { checked ->
                if (checked != -1) {
                    viewModel.apply {
                        setSortBy(category[state], checked)
                        loadCatalogue(category[state], checked)
                    }
                }
            }?.apply { show() }
        }

        return true
    }

    private fun setTitle() {
        val title = resources.getStringArray(R.array.title_toolbar)
        toolbar.title = "List ${title[state]}"
    }

    private fun loadData() {
        viewModel.apply {
            catalogeStatePosition = state
            val tag = category[state]
            loadCatalogue(tag, getSortBy(tag) ?: 0)
        }
    }

    companion object {
        const val ARG_CATALOGUE = "list"
    }
}