package com.github.hattamaulana.favorites.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.favorites.R
import com.github.hattamaulana.favorites.data.DataModel
import com.github.hattamaulana.favorites.data.Provider.IMAGE_URI
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var handleClickAction: ((id: String) -> Unit)? = null

    private var favorites =  ArrayList<DataModel>()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: DataModel) = with(view) {
            Glide.with(context)
                .load("$IMAGE_URI/w185/${data.posterPath}")
                .fitCenter()
                .into(img_movie)

            txt_title.text = data.title
            txt_rating.text = data.rating.toString()
            txt_overview.text = data.overview.takeIf { it?.length!! >= 200 }
                ?.substring(0 until 200)
                ?.split(" ".toRegex())
                ?.dropLast(1)
                ?.joinToString(" ") ?: data.overview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = favorites[position]

        holder.bind(data)
        holder.itemView.setOnClickListener {
            handleClickAction?.invoke(data.id.toString())
        }
    }

    fun update(lists: List<DataModel>) {
        favorites.clear()
        favorites.addAll(lists)
        notifyDataSetChanged()
    }
}