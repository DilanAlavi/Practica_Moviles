package com.ucb.data

import com.ucb.data.notification.INotificationService

class NotificationRepository(private val notificationService: INotificationService) {

    fun showInsufficientFundsNotification(amount: Double) {
        notificationService.showInsufficientFundsNotification(amount)
    }
}