package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for reminder post request.
 */
@Serializable
data class ReminderPostRequest(
    var dateTime: String,
) {
    constructor(): this("")
}
