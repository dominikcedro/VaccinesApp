package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val token: String,
    val refreshToken: String
)
