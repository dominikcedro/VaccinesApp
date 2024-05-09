package com.example.vaccineapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vaccineapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Service for handling Firebase messaging.
 */
class MyFirebaseMessagingService: FirebaseMessagingService() {

    /**
     * Called when a new token is generated.
     *
     * @param token The new token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    /**
     * Called when a message is received.
     *
     * @param remoteMessage The received message.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            showNotification(remoteMessage.notification)
        }
    }

    /**
     * Shows a notification.
     *
     * @param notification The notification to show.
     */
    private fun showNotification(notification: RemoteMessage.Notification?) {
        val channelId = "channel_id"
        val channelName = "channel_name"
        val notificationId = 0

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(notification?.title)
            .setContentText(notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}