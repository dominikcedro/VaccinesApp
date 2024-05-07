package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable
class UserDetails {
    var id: Int = 0
    var email: String = ""
    var firstName: String? = null
    var lastName: String? = null
    var dateOfBirth: String? = null
    var role: String = ""
}