package com.ucb.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ucb.data.datastore.ILoginDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class LoginDataSource(
    private val context: Context
) : ILoginDataStore {
    companion object {
        val EMAIL_KEY = stringPreferencesKey("email_key")
    }

    override suspend fun saveEmail(email: String) {
        context.dataStore.edit {
            preferences -> preferences[EMAIL_KEY] = email
        }
    }

    override suspend fun getEmail(): Result<String> {
        val preferences = context.dataStore.data.first()
        val email = preferences[EMAIL_KEY]
        return if (email != null) {
            Result.success(email)
        } else {
            Result.failure(EmailNotFoundError())
        }
    }
}

class EmailNotFoundError: Throwable("Email not found")