package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

/**
 * Data class for notification token request.
 */
@Serializable
data class NotificationTokenRequest(
    val token: String
)
