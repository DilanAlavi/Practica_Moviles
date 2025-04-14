package com.ucb.usecases

import com.ucb.data.MovieRepository
import com.ucb.data.NetworkResult
import com.ucb.domain.Movie
import kotlinx.coroutines.delay

class GetPopularMovies(
    val movieRepository: MovieRepository,
    val token: String
) {
    suspend fun invoke(): NetworkResult<List<Movie>> {
        return movieRepository.getPopularMovies(token = this.token)
    }
}