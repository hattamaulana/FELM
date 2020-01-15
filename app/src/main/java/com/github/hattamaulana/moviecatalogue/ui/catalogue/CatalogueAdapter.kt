package com.github.hattamaulana.moviecatalogue.ui.catalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.model.DataModel
import kotlinx.android.synthetic.main.adapter_catalogue.view.*

class CatalogueAdapter(private val mContext: Context) :
    RecyclerView.Adapter<CatalogueAdapter.ViewHolder>() {

    private var movies: ArrayList<DataModel> = ArrayList()

    private var mOnItemCallback: OnItemClickCallback? = null

    fun setData(arg: ArrayList<DataModel>) {
        movies.clear()
        movies.addAll(arg)

        notifyDataSetChanged()
    }

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
        fun bind(movie: DataModel) = with(v) {
            Glide.with(mContext)
                .load("$IMAGE_URI/w185/${movie.img}")
                .fitCenter()
                .into(img_movie)

            val overview = movie.overview!!

            txt_title.text = movie.title
            txt_rating.text = movie.rating.toString()
            txt_overview.text = if (overview.length >= 200) {
                val strCut = overview.substring(0 until 200)
                val split = strCut.split(" ".toRegex())
                val res = split.dropLast(1)

                "${res.joinToString(" ")} ..."
            } else {
                overview
            }

            v.setOnClickListener { mOnItemCallback?.onItemClicked(movie) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(movie: DataModel)
    }
}