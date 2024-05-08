package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for scheduled vaccination post request.
 */
@Serializable
data class ScheduledVaccinationPostRequest(
    val vaccineId: Long,
    val doseNumber: Int,
    val dateTime: String,
    val reminders: MutableList<ReminderPostRequest>
)
