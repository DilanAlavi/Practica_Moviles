package com.ucb.usecases

import com.ucb.data.PushNotificationRepository

class ObtainToken(
    val pushRepository: PushNotificationRepository
) {
    suspend fun getToken(): String {
    return this.pushRepository.getToken()
    }
}