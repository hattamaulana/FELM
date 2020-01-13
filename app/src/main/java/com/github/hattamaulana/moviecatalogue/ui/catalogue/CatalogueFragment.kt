package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.TheMovieDbFactory
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_catalogue.*

class CatalogueFragment : Fragment() {

    companion object {
        const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

        fun instance(index: Int): CatalogueFragment {
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)

            val fragment = CatalogueFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1
        val tag = if (arg == 0) TheMovieDbFactory.TAG_MOVIE else TheMovieDbFactory.TAG_TV
        val adapter = CatalogueAdapter(view.context)
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(CatalogueViewModel::class.java)

        viewModel.context = context
        adapter.setOnItemClckCallback(object : CatalogueAdapter.OnItemClickCallback {
            override fun onItemClicked(movie: DataModel) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_TAG, tag)
                intent.putExtra(DetailActivity.EXTRA_MOVIE_DETAIL, movie)

                startActivity(intent)
            }
        })

        viewModel.getData(tag).observe(this, Observer { listData ->
            if (listData != null) {
                progressBar.visibility = View.GONE
                adapter.setData(listData)
            } else {
                progressBar.visibility = View.VISIBLE
            }
        })

        val layout = LinearLayoutManager(view.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        recycler_movies.layoutManager = layout
        recycler_movies.adapter = adapter
    }
}
