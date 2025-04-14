package com.ucb.data.movie

import com.ucb.data.NetworkResult
import com.ucb.domain.Movie

interface IMovieRemoteDataSource {
    suspend fun fetchPopularMovies(token: String): NetworkResult<List<Movie>>
}