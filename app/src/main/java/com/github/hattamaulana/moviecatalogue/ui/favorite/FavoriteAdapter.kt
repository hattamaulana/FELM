package com.github.hattamaulana.moviecatalogue.ui.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.moviecatalogue.R
import com.github.hattamaulana.moviecatalogue.api.MovieDbContract
import com.github.hattamaulana.moviecatalogue.model.DataModel
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val TAG = this.javaClass.name

    private var dataModel = ArrayList<DataModel>()

    private lateinit var callback: OnClickCallback

    interface OnClickCallback {
        fun onItemClicked(p0: DataModel)
        fun onRemoveClicked(p0: DataModel)
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(data: DataModel): Unit = with(view) {
            Glide.with(context)
                .load("${MovieDbContract.IMAGE_URI}/w185/${data.img}")
                .fitCenter()
                .into(img_movie)

            val overview = data.overview ?: ""

            txt_category.text = data.category
            txt_title.text = data.title
            txt_rating.text = data.rating.toString()
            txt_overview.text = if (overview.length >= 100 && overview.isNotEmpty()) {
                val strCut = overview.substring(0 until 100)
                val split = strCut.split(" ".toRegex())
                val res = split.dropLast(1)

                "${res.joinToString(" ")} ..."
            } else {
                overview
            }

            view.setOnClickListener { callback.onItemClicked(data) }
            btn_remove.setOnClickListener { callback.onRemoveClicked(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
    )

    override fun getItemCount(): Int = dataModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataModel[position])
    }

    fun setData(list: List<DataModel>) {
        dataModel.clear()
        dataModel.addAll(list)

        Log.d(TAG, "setData: ${dataModel.size}")

        notifyDataSetChanged()
    }

    fun removeData(el: DataModel) {
        dataModel.remove(el)

        notifyDataSetChanged()
    }

    fun setCallback(onClickCallback: OnClickCallback) {
        callback = onClickCallback
    }
}