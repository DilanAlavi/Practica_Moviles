package com.ucb.data.git

import com.ucb.domain.Gitalias

interface ILocalDataSource {
    suspend fun save(account: Gitalias): Boolean
    suspend fun findByUser(alias: String): Gitalias
}