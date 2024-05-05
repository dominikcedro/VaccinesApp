package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for scheduled vaccination get request.
 */
@Serializable
data class ScheduledVaccinationGetRequest(
    val id: Long,
    val vaccine: Vaccine,
    val doseNumber: Int,
    val dateTime: String,
    val reminders: List<ReminderGetRequest>
)
