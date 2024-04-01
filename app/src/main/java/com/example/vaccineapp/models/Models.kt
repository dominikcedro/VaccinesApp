package com.example.vaccineapp.models

data class AuthenticationRequest(
    val email: String,
    val password: String
)

data class AuthenticationResponse(
    val token: String
)

data class RegistrationRequest(
    val email: String,
    val password: String
)