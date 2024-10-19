package com.mbaytar.newsglance.data.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mbaytar.newsglance.util.NotificationHelper

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            val title = it.title
            val body = it.body
            val notificationHelper = NotificationHelper(this)
            notificationHelper.sendNotification(title, body, -1)        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}