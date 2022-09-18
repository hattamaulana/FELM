package com.github.hattamaulana.felm.ui.favorite

import com.bumptech.glide.Glide
import com.github.hattamaulana.android.core.common.BaseAdapter
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.felm.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onItemSelected: (DataModel) -> Unit,
    private val onRemoveSelected: (DataModel) -> Unit,
) : BaseAdapter<DataModel, ItemFavoriteBinding>(
    ItemFavoriteBinding::inflate
) {
    override fun binding(
        holder: ItemFavoriteBinding, item: DataModel, position: Int
    ) = with(holder){
        Glide.with(root)
            .load("$IMAGE_URI/w185/${item.posterPath}")
            .fitCenter()
            .into(imgMovie)

        txtTitle.text = item.title
        txtRating.text = item.rating.toString()
        txtOverview.text = item.overview.takeIf { it?.length!! >= 200 }
            ?.substring(0 until 200)
            ?.split(" ".toRegex())
            ?.dropLast(1)
            ?.joinToString(" ") ?: item.overview

        root.setOnClickListener { onItemSelected(item) }

        holder.btnRemove.setOnClickListener { onRemoveSelected(item) }
    }
}