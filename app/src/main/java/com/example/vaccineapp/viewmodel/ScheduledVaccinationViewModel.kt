package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest

class ScheduledVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    val scheduledVaccinations = mutableListOf<ScheduledVaccinationGetRequest>()

    suspend fun getAdministeredVaccinations() {
        scheduledVaccinations.clear()
        scheduledVaccinations.addAll(httpService.fetchScheduledVaccines())
    }
}