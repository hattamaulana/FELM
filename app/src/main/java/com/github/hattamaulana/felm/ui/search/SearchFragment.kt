package com.github.hattamaulana.felm.ui.search

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.android.core.common.BaseFragment
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.felm.databinding.FragmentSearchBinding
import com.github.hattamaulana.felm.ui.catalogue.CatalogueWrapperFragment.Companion.ARG_CATALOGUE
import com.github.hattamaulana.felm.ui.detail.DetailActivity
import com.github.hattamaulana.felm.utils.PaginationListener
import com.github.hattamaulana.felm.utils.singleChoiceDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * A Search [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
), SearchView.OnQueryTextListener {

    private val safeArgs by navArgs<SearchFragmentArgs>()
    private val viewModel by viewModels<SearchViewModel>()

    private val viewTypes = arrayOf(TYPE_MOVIE, TYPE_TV)

    private var paramSearch: String = ""
    private var paramType: Int = -1
    private var searchQuery: String = ""
    private var page = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean? = null

    private lateinit var searchAdapter: SearchResultAdapter

    override fun initView(binding: FragmentSearchBinding) = with(binding) {
        /* Setup Toolbar */
        svSearch.isIconified = false
        svSearch.isFocusable = true
        svSearch.setOnQueryTextListener(this@SearchFragment)
        svSearch.setOnCloseListener {
            findNavController().popBackStack()
            return@setOnCloseListener true
        }

        searchAdapter = SearchResultAdapter { data ->
            findNavController().navigate(R.id.search_to_detail, Bundle().apply {
                putString(DetailActivity.EXTRA_TAG, viewTypes[paramType])
                putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, data)
                putIntArray(DetailActivity.EXTRA_GENRE_IDS, data.genres?.toIntArray())
            })
        }

        /** setup recycler view */
        val layoutManager = LinearLayoutManager(context)
        rvSearch.layoutManager = layoutManager
        rvSearch.adapter = searchAdapter

        if (paramSearch == ARG_CATALOGUE) {
            rvSearch.addOnScrollListener(scrollListener(layoutManager))
        }

        /** setup icon beside search view */
        val choices = arrayOf("Movie", "Tv Show")

        setIconTypeSearch()

        filter.setOnClickListener {
            context?.singleChoiceDialog("Lakukan Pencarian untuk :", choices) { which ->
                paramType = if (which != -1) which else 0
                setIconTypeSearch()
                search()
            }?.show()
        }
    }

    override fun initData() = with(viewModel) {
        listResult.observe(viewLifecycleOwner, Observer { searchAdapter.submitList(it) })
        totalPage.observe(viewLifecycleOwner, Observer { isLastPage = page == it })
    }

    /** Set Icon Search Tipe */
    private fun setIconTypeSearch() {
        val drawable = when (paramType) {
            0 -> resources.getDrawable(R.drawable.ic_movie)
            1 -> resources.getDrawable(R.drawable.ic_tv_show)
            else -> resources.getDrawable(R.drawable.ic_search)
        }

        binding?.filter?.setImageDrawable(drawable)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchQuery = query ?: ""
        search()

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun search() {
        if (paramSearch == ARG_CATALOGUE) {
            if (searchQuery.isNotEmpty()) viewModel.searchApi(viewTypes[paramType], searchQuery)
        } else {
            if (searchQuery.isNotEmpty()) viewModel.searchLocal(viewTypes[paramType], searchQuery)
        }
    }

    /** Scrolling Recycler View Listener to create load more feature */
    private fun scrollListener(linearLayoutManager: LinearLayoutManager) =
        object : PaginationListener(linearLayoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun isLastPage(): Boolean = isLastPage ?: true

            override fun loadMore() {
                isLoading = true
                page += 1

//                viewModel.searchApi(viewTypes[paramType], searchQuery, page) { isLoading = false }
            }
        }
}