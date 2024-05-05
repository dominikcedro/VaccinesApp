package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for administered vaccine post request.
 */
@Serializable
data class AdministeredVaccinePostRequest(
    val vaccineId: Long,
    val doseNumber: Int,
    val dateTime: String
)
