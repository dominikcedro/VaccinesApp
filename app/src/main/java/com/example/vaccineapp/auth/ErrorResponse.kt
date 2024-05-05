package com.example.vaccineapp.auth

import kotlinx.serialization.Serializable

/**
 * Data class for error response.
 */
@Serializable
data class ErrorResponse(val type: String,
                         val title: String,
                         val status: Int,
                         val detail: String,
                         val instance: String)
