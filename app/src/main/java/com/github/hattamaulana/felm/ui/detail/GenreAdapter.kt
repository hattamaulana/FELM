package com.github.hattamaulana.felm.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.model.GenreModel
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var list = arrayListOf<GenreModel>()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(string: String?) = with(view) {
            tv_genre.text = string
        }
    }

    fun add(data: GenreModel) {
        list.add(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = list[position]
        val left = if (position == 0) 32 else 8

        holder.itemView.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            .apply { setMargins(left , 8,8,8) }

        holder.bind(genre.name)
    }
}