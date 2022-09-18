package com.github.hattamaulana.felm.ui.catalogue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.android.core.common.BaseFragment
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.felm.databinding.FragmentCatalogueBinding
import com.github.hattamaulana.felm.ui.MainViewModel
import com.github.hattamaulana.felm.ui.detail.DetailActivity
import com.github.hattamaulana.felm.utils.PaginationListener
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

@AndroidEntryPoint
class CatalogueFragment : BaseFragment<FragmentCatalogueBinding>(
    FragmentCatalogueBinding::inflate
) {

    private lateinit var category: Array<String>
    private lateinit var adapter: CatalogueAdapter

    private var page = 1
    private var isLoading = false

    private val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentCatalogueBinding) = with(binding) {
        category = resources.getStringArray(R.array.category)
        adapter = CatalogueAdapter(::onItemSelected)

        /** Initialize Linear Layout Manager */
        val linearLayoutManager = LinearLayoutManager(requireContext())

        recyclerMovies.layoutManager = linearLayoutManager
        recyclerMovies.adapter = adapter
        recyclerMovies.addOnScrollListener(scrollListener(linearLayoutManager))
    }

    override fun initData() = with(viewModel) {
        catalogues.observe(viewLifecycleOwner, Observer { listData ->
            if ((listData != null) or isLoading) {
                adapter.submitList(listData)
                binding?.progressBar?.visibility = View.GONE
            } else {
                binding?.progressBar?.visibility = View.VISIBLE
            }
        })
    }

    private fun onItemSelected(movie: DataModel) {
        findNavController().navigate(R.id.catalogue_to_detail, Bundle().apply {
            val tag = if (viewModel.catalogeStatePosition == 0) TYPE_MOVIE else TYPE_TV
            putString(DetailActivity.EXTRA_TAG, tag)
            putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, movie)
            putIntArray(DetailActivity.EXTRA_GENRE_IDS, movie.genres?.toIntArray())
        })
    }

    /** Handle Scroll pada Recycler View */
    private fun scrollListener(linearLayoutManager: LinearLayoutManager) =
        object : PaginationListener(linearLayoutManager) {
            override fun isLoading(): Boolean =
                isLoading

            override fun isLastPage(): Boolean =
                page == viewModel.catalogueTotalPage

            override fun loadMore() {
                isLoading = true
                page += 1
                binding?.progressBar?.visibility = View.VISIBLE
                val tag = if (viewModel.catalogeStatePosition == 0) TYPE_MOVIE else TYPE_TV
                viewModel.loadCatalogue(tag, viewModel.getSortBy(tag) ?: 0, page) {
                    isLoading = false
                }
            }

        }

    companion object {
        fun instance(index: Int): CatalogueFragment =
            CatalogueFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }
    }
}
