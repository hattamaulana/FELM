package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hattamaulana.moviecatalogue.R
import io.github.hattamaulana.moviecatalogue.MovieModel
import kotlinx.android.synthetic.main.adapter_catalogue.view.*

class CatalogueAdapter(private val mContext: Context) :
    RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {

    var movies = ArrayList<MovieModel>()

    private var mOnItemCallback: OnItemClickCallback? = null

    fun setOnItemClckCallback(onItemClickCallback: OnItemClickCallback) {
        mOnItemCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.adapter_catalogue, parent, false)
    )

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        fun bind(movie: MovieModel) = with(v) {
            img_movie.setImageResource(movie.img)

            txt_genre.text = movie.genres
            txt_title.text = movie.title
            txt_rating.text = movie.rating.toString()
            txt_overview.text = movie.overview

            v.setOnClickListener { mOnItemCallback?.onItemClicked(movie) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(movie: MovieModel)
    }
}