package com.ucb.data

import com.ucb.data.movie.IMovieRemoteDataSource
import com.ucb.domain.Movie

class MovieRepository(
    val remoteDataSource: IMovieRemoteDataSource
) {

    suspend fun getPopularMovies(token: String): NetworkResult<List<Movie>> {
        return this.remoteDataSource.fetchPopularMovies(token)
    }
}