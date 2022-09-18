package com.github.hattamaulana.felm.ui.detail

import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.android.core.common.BaseAdapter
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.remote.MovieDbFactory
import com.github.hattamaulana.felm.databinding.ItemSimilarContentBinding

class SimilarContentAdapter(
    private val onItemSelected: (DataModel) -> Unit,
) : BaseAdapter<DataModel, ItemSimilarContentBinding>(
    ItemSimilarContentBinding::inflate
) {
    override fun binding(
        holder: ItemSimilarContentBinding, item: DataModel, position: Int
    ) = with(holder) {
        Glide.with(root)
            .load("${MovieDbFactory.IMAGE_URI}/w780/${item.posterPath}")
            .into(ivPoster)

        root.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            .apply { setMargins(if (position == 0) 32 else 8, 8, 8, 8) }
        root.setOnClickListener { onItemSelected(item) }
    }
}