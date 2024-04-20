package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable

data class ReminderPostRequest(
    private val dateTime: String,
)
