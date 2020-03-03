package com.github.hattamaulana.favorites.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.github.hattamaulana.favorites.R
import com.github.hattamaulana.favorites.data.GenreModel
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var list = arrayListOf<GenreModel>()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(string: String?) = with(view) {
            tv_genre.text = string
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val left = if (position == 0) 32 else 8
        holder.itemView.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            .apply { setMargins(left , 8,8,8) }

        holder.bind(list[position].name)
    }

    fun update(data: List<GenreModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}