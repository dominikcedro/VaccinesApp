package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import kotlinx.coroutines.launch

/**
 * ViewModel for the main menu.
 *
 * @property httpService The HTTP service for making network requests.
 */
class MainMenuViewModel(private val httpService: HttpService): ViewModel() {
    /**
     * A mutable list of vaccinations to confirm.
     */
    private val vaccinationsToConfirm = mutableListOf<ScheduledVaccinationGetRequest>()

    /**
     * Fetches the list of vaccinations to confirm from the server.
     */
    suspend fun fetchVaccinationsToConfirm() {
        vaccinationsToConfirm.clear()
        vaccinationsToConfirm.addAll(httpService.getVaccinationsToConfirm())
    }

    /**
     * @return A list of vaccinations to confirm.
     */
    fun getVaccinationsToConfirm(): List<ScheduledVaccinationGetRequest> {
        return vaccinationsToConfirm
    }

    /**
     * Confirms a vaccination.
     *
     * @param vaccination The vaccination to confirm.
     */
    fun confirmVaccination(vaccination: ScheduledVaccinationGetRequest) {
        viewModelScope.launch {
            httpService.confirmVaccination(vaccination.id)
            vaccinationsToConfirm.remove(vaccination)
        }
    }

    /**
     * Declines a vaccination.
     *
     * @param vaccination The vaccination to decline.
     */
    fun declineVaccination(vaccination: ScheduledVaccinationGetRequest) {
        viewModelScope.launch {
            httpService.deleteScheduledVaccination(vaccination.id)
            vaccinationsToConfirm.remove(vaccination)
        }
    }
}