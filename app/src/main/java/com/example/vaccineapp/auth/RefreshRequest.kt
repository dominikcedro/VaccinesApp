package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

/**
 * Data class for refresh request.
 */
@Serializable
data class RefreshRequest(
    val token: String,
    val refreshToken: String
)
