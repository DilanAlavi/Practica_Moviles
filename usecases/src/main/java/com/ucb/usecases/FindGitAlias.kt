package com.ucb.usecases

import com.ucb.data.GithubRepository
import com.ucb.data.NetworkResult
import com.ucb.domain.Gitalias
import kotlinx.coroutines.delay

class FindGitAlias(
    val githubRepository: GithubRepository
) {
    suspend fun invoke(userId: String) : NetworkResult<Gitalias> {
//        delay(100)
  //      return Gitalias("calyr", "https://avatars.githubusercontent.com/u/874321?v=4")
        return githubRepository.findbyId(userId)
    }
}