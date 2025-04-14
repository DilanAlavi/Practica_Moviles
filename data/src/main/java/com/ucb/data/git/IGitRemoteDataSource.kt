package com.ucb.data.git

import com.ucb.data.NetworkResult
import com.ucb.domain.Gitalias

interface IGitRemoteDataSource {
    suspend fun fetch(userID: String): NetworkResult<Gitalias>
}