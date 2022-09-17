package com.zhdanon.mysights2

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zhdanon.mysights2.presentation.GMapFragment
import java.text.SimpleDateFormat
import java.util.*

class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification = NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_assist)
            .setContentTitle(message.data["nickname"])
            .setContentText(message.data["message"] + " ${convertDate(message.data["timestamp"])}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(App.NOTIFICATION_ID, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun convertDate(timestamp: String?): String? {
        timestamp ?: return ""
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestamp.toLong() * 1000))
    }
}