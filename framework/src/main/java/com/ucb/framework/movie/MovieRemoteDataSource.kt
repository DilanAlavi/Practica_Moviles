package com.ucb.framework.movie

import com.ucb.data.NetworkResult
import com.ucb.data.movie.IMovieRemoteDataSource
import com.ucb.domain.Movie
import com.ucb.framework.mappers.toModel
import com.ucb.framework.service.RetrofitBuilder

class MovieRemoteDataSource(
    val retrofiService: RetrofitBuilder
): IMovieRemoteDataSource {
    override suspend fun fetchPopularMovies(token: String): NetworkResult<List<Movie>> {
        val response = retrofiService.movieService.fetchPopularMovies(token)
        if (response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.results.map { it.toModel() })
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}