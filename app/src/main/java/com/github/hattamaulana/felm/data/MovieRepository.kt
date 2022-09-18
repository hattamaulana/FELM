package com.github.hattamaulana.felm.data

import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.model.GenreModel
import com.github.hattamaulana.felm.data.remote.request.SearchRequest
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun genre(
        tag: String,
    ): Flow<List<GenreModel>>

    fun search(
        tag: String,
        query: SearchRequest,
    ): Flow<List<DataModel>>

    fun discover(
        tag: String,
        sort: Int,
        page: Int = 1,
    ): Flow<List<DataModel>>

    fun release(
        tag: String,
        date: String,
        page: Int = 1,
    ): Flow<List<DataModel>>
}