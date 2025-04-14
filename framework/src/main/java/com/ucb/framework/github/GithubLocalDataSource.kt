package com.ucb.framework.github

import android.content.Context
import com.ucb.data.git.ILocalDataSource
import com.ucb.domain.Gitalias
import com.ucb.framework.mappers.toEntity
import com.ucb.framework.mappers.toModel
import com.ucb.framework.persistence.AppRoomDatabase
import com.ucb.framework.persistence.IGitAccountDAO

class GithubLocalDataSource(val context: Context) : ILocalDataSource{
    val githubDAO: IGitAccountDAO = AppRoomDatabase.getDatabase(context).githubDao()
    override suspend fun save(account: Gitalias): Boolean {
        githubDAO.insert(account.toEntity())
        return true
    }

    override suspend fun findByUser(alias: String): Gitalias {
        return githubDAO.findByAlias(alias).toModel()
    }
}