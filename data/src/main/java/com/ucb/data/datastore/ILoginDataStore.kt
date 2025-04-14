package com.ucb.data.datastore

interface ILoginDataStore {
    suspend fun saveEmail(email: String)
    suspend fun getEmail(): Result<String>
}