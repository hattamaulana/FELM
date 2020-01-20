package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.catalogue.CatalogueFragment
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity.Companion.EXTRA_ACTIVITY
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity.Companion.EXTRA_MOVIE_DETAIL
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity.Companion.EXTRA_TAG
import kotlinx.android.synthetic.main.fragment_catalogue.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.recycler_movies

class FavoriteFragment : Fragment(), FavoriteAdapter.OnClickCallback {

    private val TAG = this.javaClass.name

    private lateinit var page: String
    private lateinit var adapter: FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1
        page = if (arg == 0) MovieDbContract.TYPE_MOVIE else MovieDbContract.TYPE_TV

        adapter = FavoriteAdapter()

        adapter.setCallback(this)
        recycler_movies.layoutManager = LinearLayoutManager(context)
        recycler_movies.adapter = adapter

        viewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadData(page, context as Context).observe(
            viewLifecycleOwner, Observer { listData -> adapter.setData(listData) }
        )
    }

    override fun onItemClicked(p0: DataModel) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(EXTRA_ACTIVITY, TAG)
        intent.putExtra(EXTRA_TAG, p0.category)
        intent.putExtra(EXTRA_MOVIE_DETAIL, p0)

        startActivity(intent)
    }

    override fun onRemoveClicked(p0: DataModel) {
        viewModel.remove(p0.id as Int, context as Context)
        adapter.removeData(p0)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

        fun instance(index: Int): FavoriteFragment {
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)

            val fragment = FavoriteFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}