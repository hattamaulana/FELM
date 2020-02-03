package com.github.hattamaulana.moviecatalogue.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener(){

    private val layoutManager = linearLayoutManager

    abstract fun isLoading(): Boolean
    abstract fun isLastPage(): Boolean
    abstract fun loadMore()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val totalVisibleItem = layoutManager.childCount + firstVisibleItemPosition

        if (!isLoading() && !isLastPage()) {
            if (totalVisibleItem >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMore();
            }
        }
    }

    companion object {
        const val PAGE_START = 1

        val TAG = PaginationListener::class.java.simpleName
    }
}