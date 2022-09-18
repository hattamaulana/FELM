package com.github.hattamaulana.felm.data.repository

import android.content.Context
import com.github.hattamaulana.felm.data.MovieRepository
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.model.GenreModel
import com.github.hattamaulana.felm.data.remote.MovieService
import com.github.hattamaulana.felm.data.remote.request.DiscoverRequest
import com.github.hattamaulana.felm.data.remote.request.SearchRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    context: Context,
    private val service: MovieService
) : Repository(context), MovieRepository {
    override fun genre(tag: String): Flow<List<GenreModel>> = getResponse {
        service.genre(tag)
    }

    override fun search(tag: String, query: SearchRequest): Flow<List<DataModel>> = getResponse {
        service.search(tag, query)
    }

    override fun discover(
        tag: String,
        sort: Int,
        page: Int,
    ): Flow<List<DataModel>> = getResponse {
        service.discover(tag, DiscoverRequest(sortBy = sort, page = page))
    }

    override fun release(
        tag: String,
        date: String,
        page: Int,
    ): Flow<List<DataModel>> = getResponse {
        service.discover(tag, DiscoverRequest(lte = date, gte = date, page = page))
    }
}