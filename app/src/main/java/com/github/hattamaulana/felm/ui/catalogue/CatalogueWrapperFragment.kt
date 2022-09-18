package com.github.hattamaulana.felm.ui.catalogue

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.hattamaulana.android.core.common.BaseFragment
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.databinding.FragmentCatalogueWrapperBinding
import com.github.hattamaulana.felm.ui.MainViewModel
import com.github.hattamaulana.felm.ui.TabLayoutAdapter
import com.github.hattamaulana.felm.utils.TabChangeListener
import com.github.hattamaulana.felm.utils.singleChoiceDialog
import dagger.hilt.android.AndroidEntryPoint

private var state: Int = 0

@AndroidEntryPoint
class CatalogueWrapperFragment : BaseFragment<FragmentCatalogueWrapperBinding>(
    FragmentCatalogueWrapperBinding::inflate
) {

    private lateinit var category: Array<String>
    private lateinit var sortBy: Array<String>

    private val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentCatalogueWrapperBinding) = with(binding) {
        setTitle()
        setHasOptionsMenu(true)

        requireActivity().setActionBar(toolbar)

        category = resources.getStringArray(R.array.category)
        sortBy = resources.getStringArray(R.array.filtering_data)

        viewPager.adapter = TabLayoutAdapter(context as Context, childFragmentManager) {
            CatalogueFragment.instance(it)
        }

        viewPager.setCurrentItem(state, true)
        viewPager.addOnPageChangeListener(TabChangeListener { position ->
            state = position

            setTitle()
            initData()
        })

        tabs.setupWithViewPager(viewPager)
    }

    override fun initData() = with(viewModel) {
        catalogeStatePosition = state

        val tag = category[state]
        loadCatalogue(tag, getSortBy(tag) ?: 0)
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

        binding?.toolbar?.title = "List ${title[state]}"
    }


    companion object {
        const val ARG_CATALOGUE = "list"
    }
}