package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable

data class ReminderGetRequest(
    val dateTime: String,
    val id: Long
)
