package com.ucb.usecases

import com.ucb.data.LoginRepository

class GetEmailKey(
    val loginRepository: LoginRepository
) {
    suspend fun invoke(): Result<String> {
       return loginRepository.getEmail()
    }
}