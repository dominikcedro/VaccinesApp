package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class AdministeredVaccinePostRequest(
    val vaccineId: Long,
    val doseNumber: Int,
    val date: String
)
