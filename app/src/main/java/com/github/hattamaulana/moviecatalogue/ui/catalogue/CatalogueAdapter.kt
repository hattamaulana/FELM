package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import kotlinx.android.synthetic.main.item_catalogue.view.*

class CatalogueAdapter(private val mContext: Context) :
    RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {

    private var movies: ArrayList<DataModel> = ArrayList()
    private var mOnItemCallback: OnItemClickCallback? = null

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: DataModel) = with(view) {
            Glide.with(mContext)
                .load("$IMAGE_URI/w185/${movie.posterPath}")
                .fitCenter()
                .into(img_movie)

            val overview = movie.overview as String

            txt_title.text = movie.title
            txt_rating.text = movie.rating.toString()
            txt_overview.text = if (overview.length >= 200) {
                val string = overview.substring(0 until 200)
                    .split(" ".toRegex())
                    .dropLast(1)
                    .joinToString(" ")

                "$string ..."
            } else {
                overview
            }

            view.setOnClickListener { mOnItemCallback?.onItemClicked(movie) }
        }
    }

    /**
     *
     */
    interface OnItemClickCallback {
        fun onItemClicked(movie: DataModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.item_catalogue, parent, false)
    )

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun setData(arg: List<DataModel>) {
        movies.clear()
        movies.addAll(arg)

        notifyDataSetChanged()
    }

    fun setOnItemClckCallback(onItemClickCallback: OnItemClickCallback) {
        mOnItemCallback = onItemClickCallback
    }
}