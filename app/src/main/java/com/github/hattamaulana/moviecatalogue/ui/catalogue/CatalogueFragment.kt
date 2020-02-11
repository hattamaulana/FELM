package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import com.github.hattamaulana.moviecatalogue.utils.PaginationListener
import kotlinx.android.synthetic.main.fragment_catalogue.*

private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

class CatalogueFragment : Fragment(), CatalogueAdapter.OnItemClickCallback {

    private lateinit var mTag: String
    private lateinit var adapter: CatalogueAdapter
    private lateinit var viewModel: CatalogueViewModel

    private var page = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        /** Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1

        mTag = if (arg == 0) TYPE_MOVIE else TYPE_TV
        adapter = CatalogueAdapter(view.context as Context)
        adapter.setOnItemClckCallback(this)

        /** Initialize Linear Layout Manager */
        val linearLayoutManager = LinearLayoutManager(view.context)

        recycler_movies.layoutManager = linearLayoutManager
        recycler_movies.adapter = adapter
        recycler_movies.addOnScrollListener(scrollListener(linearLayoutManager))

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(CatalogueViewModel::class.java)
        viewModel.context = context
        viewModel.loadData(mTag)
        viewModel.listData.observe(viewLifecycleOwner, observing())
    }

    override fun onItemClicked(movie: DataModel) {
        findNavController().navigate(R.id.catalogue_to_detail, Bundle().apply {
            putString(DetailActivity.EXTRA_TAG, mTag)
            putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, movie)
            putIntArray(DetailActivity.EXTRA_GENRE_IDS, movie.genres?.toIntArray())
        })
    }

    /** Handle Scroll pada Recycler View */
    private fun scrollListener(linearLayoutManager: LinearLayoutManager) =
        object : PaginationListener(linearLayoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun isLastPage(): Boolean {
                if (viewModel.totalPage == null) return false

                return page == viewModel.totalPage
            }

            override fun loadMore() {
                isLoading = true
                page += 1

                progressBar.visibility = View.VISIBLE
                viewModel.loadMore(mTag, page) {
                    isLoading = false
                }
            }

        }

    /** Handle ketika ada data ada yang berubah */
    private fun observing() =
        Observer<List<DataModel>> { listData ->
            if ((listData != null) or isLoading) {
                adapter.setData(listData)
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
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
