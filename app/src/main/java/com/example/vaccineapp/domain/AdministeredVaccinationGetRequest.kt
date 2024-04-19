package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class AdministeredVaccinationGetRequest(
    val id: Long,
    val vaccine: Vaccine,
    val doseNumber: Int,
    val dateTime: String
)
