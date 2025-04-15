package com.ucb.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookResponseDto(
    val numFound: Int,
    val start: Int,
    val docs: List<BookDto>
)

@JsonClass(generateAdapter = true)
data class BookDto(
    val key: String,
    val title: String,
    @Json(name = "author_name") val authorName: List<String>? = null,
    @Json(name = "first_publish_year") val firstPublishYear: Int? = null,
    @Json(name = "cover_i") val coverId: Int? = null,
    @Json(name = "cover_edition_key") val coverEditionKey: String? = null
)