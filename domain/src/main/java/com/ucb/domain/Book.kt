package com.ucb.domain

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val key: String,
    val title: String,
    val author_name: List<String> = emptyList(),
    val first_publish_year: Int? = null,
    val cover_i: Int? = null,
    val cover_edition_key: String? = null
)