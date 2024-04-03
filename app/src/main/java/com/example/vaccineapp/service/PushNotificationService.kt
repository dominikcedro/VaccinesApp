package com.example.vaccineapp.service

import HttpService
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PushNotificationService(private val httpService: HttpService): FirebaseMessagingService() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        GlobalScope.launch {
            httpService.updateNotificicationToken(token)
        }
    }
}