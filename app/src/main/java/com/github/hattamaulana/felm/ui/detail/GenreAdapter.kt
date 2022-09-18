package com.github.hattamaulana.felm.ui.detail

import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.github.hattamaulana.android.core.common.BaseAdapter
import com.github.hattamaulana.felm.data.model.GenreModel
import com.github.hattamaulana.felm.databinding.ItemGenreBinding

class GenreAdapter : BaseAdapter<GenreModel, ItemGenreBinding>(
    ItemGenreBinding::inflate
) {

    override fun binding(
        holder: ItemGenreBinding, item: GenreModel, position: Int
    ) = with(holder) {
        tvGenre.text = item.name
        root.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            .apply { setMargins(if (position == 0) 32 else 8, 8, 8, 8) }
    }
}