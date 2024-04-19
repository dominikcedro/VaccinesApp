package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

@Serializable
data class NotificationTokenRequest(
    val token: String
)
