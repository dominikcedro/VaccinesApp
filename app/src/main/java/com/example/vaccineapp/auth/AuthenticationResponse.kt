package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
        var token: String,
        var refreshToken: String,
        var expirationDate: String
)