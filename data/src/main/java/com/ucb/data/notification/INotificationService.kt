package com.ucb.data.notification

interface INotificationService {
    fun showInsufficientFundsNotification(amount: Double)
}