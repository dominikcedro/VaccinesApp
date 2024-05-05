package com.example.vaccineapp.domain

import kotlinx.serialization.Serializable

/**
 * Data class for vaccine.
 */
@Serializable
data class Vaccine(
    val id: Long,
    val name: String,
    val recommendedAgeMonthsLowerBound: Int,
    val recommendedAgeMonthsUpperBound: Int?,
    val doses: List<Dose>
)
