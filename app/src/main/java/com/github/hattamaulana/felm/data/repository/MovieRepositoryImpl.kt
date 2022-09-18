package com.github.hattamaulana.felm.data.repository

import com.github.hattamaulana.felm.data.MovieRepository
import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.model.GenreModel
import com.github.hattamaulana.felm.data.remote.MovieService
import com.github.hattamaulana.felm.data.remote.request.SearchRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService
): MovieRepository {
    override fun genre(tag: String): Flow<List<GenreModel>> {
        TODO("Not yet implemented")
    }

    override fun search(tag: String, query: SearchRequest): Flow<List<DataModel>> {
        TODO("Not yet implemented")
    }

    override fun discover(tag: String, sort: Int, page: Int): Flow<List<DataModel>> {
        TODO("Not yet implemented")
    }

    override fun release(tag: String, date: String, page: Int): Flow<List<DataModel>> {
        TODO("Not yet implemented")
    }

}