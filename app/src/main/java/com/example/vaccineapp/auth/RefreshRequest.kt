package com.mwdziak.fitness_mobile_client.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val token: String,
    val refreshToken: String
)
