package com.ucb.usecases

import com.ucb.data.GithubRepository
import com.ucb.domain.Gitalias

class SaveGitalias(
    val repository: GithubRepository
) {
    suspend fun invoke(account: Gitalias) {
        repository.save(account)
    }
}