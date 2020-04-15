package com.github.hattamaulana.felm.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.felm.data.api.MovieDbFactory
import com.github.hattamaulana.felm.data.model.DataModel
import kotlinx.android.synthetic.main.item_search_result.view.*

class SearchResultViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(data: DataModel) = with(view) {
        /* Set Image View */
        val imgPath = data.posterPath
        Glide.with(view)
            .load("${MovieDbFactory.IMAGE_URI}/w185/$imgPath")
            .fitCenter()
            .into(iv_movie)

        /* Set Title */
        tv_title.text = data.title
    }

}