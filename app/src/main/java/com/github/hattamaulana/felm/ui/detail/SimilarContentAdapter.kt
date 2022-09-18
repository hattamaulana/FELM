package com.github.hattamaulana.felm.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.remote.MovieDbFactory
import com.github.hattamaulana.felm.data.model.DataModel
import kotlinx.android.synthetic.main.item_similar_content.view.*

class SimilarContentAdapter : RecyclerView.Adapter<SimilarContentAdapter.ViewHolder>() {

    private lateinit var onClickListener: (id: DataModel) -> Unit

    private var list = listOf<DataModel>()

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(dataModel: DataModel) = with(view) {
            val string = dataModel.posterPath

            Glide.with(this)
                .load("${MovieDbFactory.IMAGE_URI}/w780/$string")
                .into(iv_poster)
        }
    }

    fun update(data: List<DataModel>) {
        this.list = data
        notifyDataSetChanged()
    }

    fun setOnCLickListener(callback: (id: DataModel) -> Unit) {
        onClickListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_similar_content, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val left = if (position == 0) 32 else 8

        holder.itemView.apply {
            layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
                .apply { setMargins(left , 8,8,8) }

            setOnClickListener { onClickListener(data) }
        }

        holder.bind(data)
    }

}