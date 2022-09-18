package com.github.hattamaulana.felm.ui.catalogue

import com.bumptech.glide.Glide
import com.github.hattamaulana.android.core.common.BaseAdapter
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.felm.databinding.ItemCatalogueBinding

class CatalogueAdapter(
    private val onItemSelected: (DataModel) -> Unit
) : BaseAdapter<DataModel, ItemCatalogueBinding>(
    ItemCatalogueBinding::inflate
) {

    override fun binding(
        holder: ItemCatalogueBinding, item: DataModel, position: Int
    ) = with(holder) {
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
    }
}

