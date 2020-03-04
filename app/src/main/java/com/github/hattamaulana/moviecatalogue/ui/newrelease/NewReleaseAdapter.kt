package com.github.hattamaulana.moviecatalogue.ui.newrelease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.data.api.MovieDbFactory.IMAGE_URI
import com.github.hattamaulana.moviecatalogue.data.model.DataModel
import kotlinx.android.synthetic.main.item_favorite.view.*

class NewReleaseAdapter : RecyclerView.Adapter<NewReleaseAdapter.ViewHodler>() {

    var handleOnClickAction: ((data: DataModel)-> Unit)? = null

    private val data = arrayListOf<DataModel>()

    inner class ViewHodler(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: DataModel) = with(view) {
            Glide.with(context)
                .load("$IMAGE_URI/w185/${item.posterPath}")
                .fitCenter()
                .into(img_movie)

            txt_title.text = item.title
            txt_rating.text = item.rating.toString()
            txt_overview.text = item.overview.takeIf { it?.length!! >= 200 }
                ?.substring(0 until 200)
                ?.split(" ".toRegex())
                ?.dropLast(1)
                ?.joinToString(" ") ?: item.overview

            setOnClickListener { handleOnClickAction?.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalogue, parent, false)
        return ViewHodler(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHodler, position: Int) {
        holder.bind(data[position])
    }

    fun update(list: List<DataModel>) {
        data.clear()
        data.addAll(list)

        notifyDataSetChanged()
    }

}