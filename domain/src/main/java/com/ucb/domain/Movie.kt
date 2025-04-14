package com.ucb.domain

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    val posterPath: String,
    val overview: String
)