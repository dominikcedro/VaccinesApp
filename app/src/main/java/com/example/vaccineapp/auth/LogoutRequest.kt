package com.example.vaccineapp.auth
import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val token: String,
    val refreshToken: String
)
