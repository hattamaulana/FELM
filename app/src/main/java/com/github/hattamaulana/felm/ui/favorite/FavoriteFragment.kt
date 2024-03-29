package com.github.hattamaulana.felm.ui.favorite

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hattamaulana.android.core.common.BaseFragment
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.databinding.FragmentFavoriteBinding
import com.github.hattamaulana.felm.ui.MainViewModel
import com.github.hattamaulana.felm.ui.detail.DetailActivity

private const val ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(
    FragmentFavoriteBinding::inflate
)  {

    private val adapter = FavoriteAdapter(::onItemClicked, ::onRemoveClicked)
    private val viewModel: MainViewModel by activityViewModels()

    override fun initView(binding: FragmentFavoriteBinding) = with(binding) {
        recyclerFavorite.layoutManager = LinearLayoutManager(context)
        recyclerFavorite.adapter = adapter
    }

    override fun initData() = with(viewModel) {
        favorites.observe(viewLifecycleOwner) { list -> adapter.submitList(list) }
    }

    private fun onItemClicked(p0: DataModel) {
        findNavController().navigate(R.id.favorite_to_detail, Bundle().apply {
            putString(DetailActivity.EXTRA_TAG, p0.category)
            putIntArray(DetailActivity.EXTRA_GENRE_IDS, p0.genres?.toIntArray())
            putParcelable(DetailActivity.EXTRA_MOVIE_DETAIL, p0)
        })
    }

    private fun onRemoveClicked(p0: DataModel) {
        viewModel.removeDataFromFavorite(p0)

        val newList = adapter.currentList.toMutableList()
            .apply { remove(p0) }

        adapter.submitList(newList)
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