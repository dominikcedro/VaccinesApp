package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import kotlinx.coroutines.launch

class MainMenuViewModel(private val httpService: HttpService): ViewModel() {
    private val vaccinationsToConfirm = mutableListOf<ScheduledVaccinationGetRequest>()
    suspend fun fetchVaccinationsToConfirm() {
        vaccinationsToConfirm.clear()
        vaccinationsToConfirm.addAll(httpService.getVaccinationsToConfirm())
    }

    fun getVaccinationsToConfirm(): List<ScheduledVaccinationGetRequest> {
        return vaccinationsToConfirm
    }

    fun confirmVaccination(vaccination: ScheduledVaccinationGetRequest) {
        viewModelScope.launch {
            httpService.confirmVaccination(vaccination.id)
            vaccinationsToConfirm.remove(vaccination)
        }
    }

    fun declineVaccination(vaccination: ScheduledVaccinationGetRequest) {
        viewModelScope.launch {
            httpService.deleteScheduledVaccination(vaccination.id)
            vaccinationsToConfirm.remove(vaccination)
        }
    }
}