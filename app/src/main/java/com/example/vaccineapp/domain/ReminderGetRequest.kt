package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for reminder get request.
 */
@Serializable

data class ReminderGetRequest(
    val dateTime: String,
    val id: Long
)
