package com.ucb.framework.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IGitAccountDAO {

    @Query("SELECT * FROM github_account")
    fun getAccounts(): List<GitAccount>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gitAccount: GitAccount)

    @Query("DELETE FROM github_account")
    suspend fun deleteAll()

    @Query("SELECT * FROM github_account WHERE alias = :alias LIMIT 1")
    suspend fun findByAlias(alias: String): GitAccount
}