package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable
class UserDetails {
    var email: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var dateOfBirth: String = ""
    var role: String = ""
}