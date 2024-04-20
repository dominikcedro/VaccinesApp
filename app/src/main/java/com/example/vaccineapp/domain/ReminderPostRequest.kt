package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable

data class ReminderPostRequest(
    var dateTime: String,
) {
    constructor(): this("")
}
