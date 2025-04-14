package com.ucb.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MovieResponseDto(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val results: List<MovieDto>
)