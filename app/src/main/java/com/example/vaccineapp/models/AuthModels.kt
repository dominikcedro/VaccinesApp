package com.example.vaccineapp.models

data class AuthenticationRequest(val username: String, val password: String)
data class RegistrationRequest(val username: String, val password: String, val email: String)
data class AuthenticationResponse(val id: String, val username: String, val token: String)