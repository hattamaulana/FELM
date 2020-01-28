package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_MOVIE
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.TYPE_TV
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import com.github.hattamaulana.moviecatalogue.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_favorite.*

private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

class FavoriteFragment : Fragment(), FavoriteAdapter.OnClickCallback {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        /** Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = arguments?.getInt(ARG_SECTION_NUMBER, 1) ?: 1
        val tag = if (arg == 0) TYPE_MOVIE else TYPE_TV

        adapter = FavoriteAdapter()
        adapter.setCallback(this)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FavoriteViewModel::class.java)
        viewModel.context = context
        viewModel.loadData(tag).observe(viewLifecycleOwner, Observer { listData ->
            adapter.setData(listData)
        })

        recycler_favorite.layoutManager = LinearLayoutManager(context)
        recycler_favorite.adapter = adapter
    }

    override fun onItemClicked(p0: DataModel) {
        findNavController().navigate(R.id.favorite_to_detail, Bundle().apply {
            putString(DetailActivity.EXTRA_TAG, p0.category)
            putIntArray(DetailActivity.EXTRA_GENRE_IDS, p0.genres?.toIntArray())
            putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, p0)
        })
    }

    override fun onRemoveClicked(p0: DataModel) {
        viewModel.remove(p0)
        adapter.removeData(p0)
    }

    companion object {
        fun instance(index: Int): FavoriteFragment =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }
    }
}