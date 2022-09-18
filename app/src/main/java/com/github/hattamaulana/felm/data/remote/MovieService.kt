package com.github.hattamaulana.felm.data.remote

import com.github.hattamaulana.felm.data.model.DataModel
import com.github.hattamaulana.felm.data.model.GenreModel
import com.github.hattamaulana.felm.data.remote.request.DiscoverRequest
import com.github.hattamaulana.felm.data.remote.request.SearchRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("/genre/{tag}/list")
    fun genre(@Path("tag") tag: String): Response<List<GenreModel>>

    @GET("/discover/{tag}")
    fun discover(
        @Path("tag") tag: String,
        @Body body: DiscoverRequest
    ): Response<List<DataModel>>

    @GET("{tag}/{id}/similar")
    fun similar(
        @Path("tag") tag: String,
        @Path("id") id: Int,
    ): Response<List<DataModel>>

    @GET("search/{tag}")
    fun search(
        @Path("tag") tag: String,
        @Body body: SearchRequest,
    ): Response<List<DataModel>>
}