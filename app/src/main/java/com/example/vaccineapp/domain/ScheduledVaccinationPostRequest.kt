package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable

data class ScheduledVaccinationPostRequest(
    val vaccineId: Long,
    val doseNumber: Int,
    val dateTime: String,
    val reminderPostRequests: List<ReminderPostRequest>
)
