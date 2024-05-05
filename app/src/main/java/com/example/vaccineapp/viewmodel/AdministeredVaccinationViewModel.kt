package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest

/**
 * ViewModel for administered vaccinations.
 *
 * @property httpService The HTTP service for making network requests.
 */
class AdministeredVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    /**
     * A mutable list of administered vaccinations.
     */
    val administeredVaccinations = mutableListOf<AdministeredVaccinationGetRequest>()

    /**
     * Fetches the list of administered vaccinations from the server.
     */
    suspend fun getAdministeredVaccinations() {
        administeredVaccinations.clear()
        administeredVaccinations.addAll(httpService.fetchAdministeredVaccines())
    }
}