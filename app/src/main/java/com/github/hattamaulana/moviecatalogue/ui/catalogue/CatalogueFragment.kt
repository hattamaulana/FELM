package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.TYPE_TV
import com.github.hattamaulana.moviecatalogue.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_catalogue.*

class CatalogueFragment : Fragment(), CatalogueAdapter.OnItemClickCallback {

    private val TAG = this.javaClass.name

    private lateinit var mTag: String
    private lateinit var mAdapter: CatalogueAdapter
    private lateinit var mViewModel: CatalogueViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ;")

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1

        mTag = if (arg == 0) TYPE_MOVIE else TYPE_TV
        mAdapter = CatalogueAdapter(view.context as Context)
        mAdapter.setOnItemClckCallback(this)

        recycler_movies.layoutManager = LinearLayoutManager(view.context)
        recycler_movies.adapter = mAdapter

        mViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(CatalogueViewModel::class.java)
        mViewModel.context = context
        mViewModel.getData(mTag).observe(viewLifecycleOwner, Observer { listData ->
            if (listData != null) {
                mAdapter.setData(listData)

                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: mTag=$mTag")
    }

    override fun onItemClicked(movie: DataModel) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_TAG, mTag)
        intent.putExtra(DetailActivity.EXTRA_MOVIE_DETAIL, movie)

        startActivity(intent)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

        fun instance(index: Int): CatalogueFragment {
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)

            val fragment = CatalogueFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}
