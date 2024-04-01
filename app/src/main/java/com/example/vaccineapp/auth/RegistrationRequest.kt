package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
        var email: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var dateOfBirth: String
)