package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest

/**
 * ViewModel for scheduled vaccinations.
 *
 * @property httpService The HTTP service for making network requests.
 */
class ScheduledVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    /**
     * A mutable list of scheduled vaccinations.
     */
    val scheduledVaccinations = mutableListOf<ScheduledVaccinationGetRequest>()

    /**
     * Fetches the list of scheduled vaccinations from the server.
     */
    suspend fun getScheduledVaccinations() {
        scheduledVaccinations.clear()
        scheduledVaccinations.addAll(httpService.fetchScheduledVaccines())
    }
}