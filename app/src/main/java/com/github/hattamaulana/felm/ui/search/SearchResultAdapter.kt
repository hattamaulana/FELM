package com.github.hattamaulana.felm.ui.search

import com.bumptech.glide.Glide
import com.github.hattamaulana.android.core.common.BaseAdapter
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbFactory
import com.github.hattamaulana.felm.databinding.ItemSearchResultBinding

class SearchResultAdapter(
    private val onItemSelected: (DataModel) -> Unit
) : BaseAdapter<DataModel, ItemSearchResultBinding>(
    ItemSearchResultBinding::inflate,
) {
    override fun binding(
        holder: ItemSearchResultBinding, item: DataModel, position: Int
    ) = with(holder) {
        Glide.with(root)
            .load("${MovieDbFactory.IMAGE_URI}/w185/${item.posterPath}")
            .fitCenter()
            .into(ivMovie)

        tvTitle.text = item.title
        root.setOnClickListener { onItemSelected(item) }
    }
}