package com.github.hattamaulana.felm.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener(){

    private val layoutManager = linearLayoutManager

    /** Check is Loading */
    abstract fun isLoading(): Boolean

    /** Checking Las Page */
    abstract fun isLastPage(): Boolean

    /** Do Load more */
    abstract fun loadMore()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val totalVisibleItem = layoutManager.childCount + firstVisibleItemPosition

        if (!isLoading() && !isLastPage()) {
            if (totalVisibleItem >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMore()
            }
        }
    }
}