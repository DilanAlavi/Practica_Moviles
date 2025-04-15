package com.ucb.framework.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ucb.framework.R
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val CHANNEL_ID = "finance_notifications"
        private const val CHANNEL_NAME = "Finanzas"
        private const val CHANNEL_DESCRIPTION = "Notificaciones de la aplicación de finanzas"
    }

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showInsufficientBalanceNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Advertencia de Saldo")
            .setContentText("Has registrado un gasto sin tener ingresos suficientes para respaldarlo.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}