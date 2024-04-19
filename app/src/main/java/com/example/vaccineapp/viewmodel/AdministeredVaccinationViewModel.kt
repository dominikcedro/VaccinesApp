package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest

class AdministeredVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    val administeredVaccinations = mutableListOf<AdministeredVaccinationGetRequest>()

    suspend fun getAdministeredVaccinations() {
        administeredVaccinations.clear()
        administeredVaccinations.addAll(httpService.fetchAdministeredVaccines())
    }
}