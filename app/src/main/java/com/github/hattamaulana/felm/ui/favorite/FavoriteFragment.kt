package com.github.hattamaulana.felm.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.ui.MainViewModel
import com.github.hattamaulana.felm.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_favorite.*

private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

class FavoriteFragment : Fragment(), FavoriteAdapter.OnClickCallback {

    private lateinit var adapter: FavoriteAdapter

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        /** Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter()
            .apply { setCallback(this@FavoriteFragment) }
        recycler_favorite.layoutManager = LinearLayoutManager(context)
        recycler_favorite.adapter = adapter

        viewModel.favorites.observe(viewLifecycleOwner, Observer { list ->
            adapter.setData(list)
        })
    }

    override fun onItemClicked(p0: DataModel) {
        findNavController().navigate(R.id.favorite_to_detail, Bundle().apply {
            putString(DetailActivity.EXTRA_TAG, p0.category)
            putIntArray(DetailActivity.EXTRA_GENRE_IDS, p0.genres?.toIntArray())
            putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, p0)
        })
    }

    override fun onRemoveClicked(p0: DataModel) {
        viewModel.removeDataFromFavorite(p0)
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