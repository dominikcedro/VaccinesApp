package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest(
    var email: String,
    var password: String
)