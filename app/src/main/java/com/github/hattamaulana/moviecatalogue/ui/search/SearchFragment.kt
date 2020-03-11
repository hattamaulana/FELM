package com.github.hattamaulana.moviecatalogue.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.moviecatalogue.ui.catalogue.CatalogueWrapperFragment.Companion.ARG_CATALOGUE
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import com.github.hattamaulana.moviecatalogue.utils.PaginationListener
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A Search [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment :
    Fragment(),
    SearchView.OnQueryTextListener {

    private lateinit var adapter: SearchResultAdapter
    private lateinit var viewModel: SearchViewModel

    private var paramSearch: String = ""
    private var paramType: Int = -1
    private var searchQuery: String = ""
    private var page = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean? = null

    /* Get Arguments from bundle */
    private val safeArgs by navArgs<SearchFragmentArgs>()
    private val viewTypes = arrayOf(TYPE_MOVIE, TYPE_TV)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        paramSearch = safeArgs.ARGSEARCH
        paramType = safeArgs.ARGTYPE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Setup Toolbar */
        sv_search.isIconified = false
        sv_search.isFocusable = true
        sv_search.setOnQueryTextListener(this)
        sv_search.setOnCloseListener {
            findNavController().popBackStack()
            return@setOnCloseListener true
        }

        initAdapter()
        initViewModel()

        /** setup recycler view */
        val layoutManager = LinearLayoutManager(context)
        rv_search.layoutManager = layoutManager
        rv_search.adapter = adapter

        if (paramSearch == ARG_CATALOGUE) {
            rv_search.addOnScrollListener(scrollListener(layoutManager))
        }

        /** setup icon beside search view */
        val choices = arrayOf("Movie", "Tv Show")
        setIconTypeSearch()
        filter.setOnClickListener {
            AlertDialog.Builder(context as Context)
                .setTitle("Lakukan Pencarian di :")
                .setSingleChoiceItems(choices, paramType) { _, which -> paramType = which }
                .setPositiveButton("OK") { _, _ ->
                    setIconTypeSearch()
                    search()
                }
                .create()
                .show()
        }
    }

    /** Set Icon Search Tipe */
    private fun setIconTypeSearch() {
        val drawable = when (paramType) {
            0 -> resources.getDrawable(R.drawable.ic_movie)
            1 -> resources.getDrawable(R.drawable.ic_tv_show)
            else -> resources.getDrawable(R.drawable.ic_search)
        }

        filter.setImageDrawable(drawable)
    }

    /** Initialize and setup Adapter */
    private fun initAdapter() {
        adapter = SearchResultAdapter()
        adapter.setOnClickListener { data ->
            findNavController().navigate(R.id.search_to_detail, Bundle().apply {
                putString(DetailActivity.EXTRA_TAG, viewTypes[paramType])
                putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, data)
                putIntArray(DetailActivity.EXTRA_GENRE_IDS, data.genres?.toIntArray())
            })
        }
    }

    /** Initialize ViewModel and Observing data result searching */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(SearchViewModel::class.java)
            .apply { context = this@SearchFragment.context }

        viewModel.listResult.observe(viewLifecycleOwner, Observer { adapter.update(it) })
        viewModel.totalPage.observe(viewLifecycleOwner, Observer { isLastPage = page == it })
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

                viewModel.searchApi(viewTypes[paramType], searchQuery, page) { isLoading = false }
            }

        }
}