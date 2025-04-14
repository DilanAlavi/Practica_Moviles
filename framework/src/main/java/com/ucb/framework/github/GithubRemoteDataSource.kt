package com.ucb.framework.github

import com.ucb.data.NetworkResult
import com.ucb.data.git.IGitRemoteDataSource
import com.ucb.domain.Gitalias
import com.ucb.framework.mappers.toModel
import com.ucb.framework.service.RetrofitBuilder

class GithubRemoteDataSource(
    val retrofiService: RetrofitBuilder
) : IGitRemoteDataSource {
    override suspend fun fetch(userID: String): NetworkResult<Gitalias> {
        val response = retrofiService.apiService.getInfoAvatar(userID)
        if(response.isSuccessful) {
            return NetworkResult.Success(response.body()!!.toModel())
        } else {
            return NetworkResult.Error(response.message())
        }
    }
}
