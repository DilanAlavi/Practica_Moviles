package com.ucb.usecases

import com.ucb.data.NotificationRepository
import javax.inject.Inject

class NotifyInsufficientFunds @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    fun invoke(amount: Double) {
        notificationRepository.showInsufficientFundsNotification(amount)
    }
}