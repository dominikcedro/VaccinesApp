package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for dose.
 */
@Serializable
data class Dose(
    val id: Long,
    val doseNumber: Int,
    val monthsAfterPreviousDose: Int?
)
