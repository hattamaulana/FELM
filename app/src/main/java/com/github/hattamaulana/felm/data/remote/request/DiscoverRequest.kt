package com.github.hattamaulana.felm.data.remote.request

import com.google.gson.annotations.SerializedName

data class DiscoverRequest(
    @SerializedName("primary_release_date.gte") val gte: String? = null,
    @SerializedName("primary_release_date.lte") val lte: String? = null,
    @SerializedName("sort_by") val sortBy: Int? = null,
    @SerializedName("page") val page: Int,
)