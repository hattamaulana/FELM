package com.github.hattamaulana.felm.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hattamaulana.felm.R
import com.github.hattamaulana.felm.data.model.DataModel

class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

    private val listResult = mutableListOf<DataModel>()

    private lateinit var clickCallback: (p0: DataModel)-> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)

        return SearchResultViewHolder(view)
    }

    override fun getItemCount(): Int = listResult.size

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(listResult[position])
        holder.itemView.setOnClickListener { clickCallback(listResult[position]) }
    }

    /** Update data melalui fungsi ini */
    fun update(args: List<DataModel>) {
        listResult.clear()
        listResult.addAll(args)

        notifyDataSetChanged()
    }

    /** Set OnCLick Listener */
    fun setOnClickListener(onClick: (data: DataModel)-> Unit) {
        clickCallback = onClick
    }
}