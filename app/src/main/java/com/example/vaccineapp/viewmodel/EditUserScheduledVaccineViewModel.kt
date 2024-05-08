package com.example.vaccineapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import HttpService
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.service.TokenManager
import kotlinx.coroutines.launch

class EditUserScheduledVaccineViewModel(private val tokenManager: TokenManager, private val httpService: HttpService) : ViewModel() {
    private val _scheduledVaccination = MutableLiveData<ScheduledVaccinationGetRequest?>()
    val scheduledVaccination: LiveData<ScheduledVaccinationGetRequest?> = _scheduledVaccination

    fun fetchScheduledVaccination(vaccineId: Long) {
        viewModelScope.launch {
            _scheduledVaccination.value = httpService.getSingleScheduledVaccination(tokenManager.getJwtToken() ?: "", vaccineId)
        }
    }

    // Uncomment and modify the following method if you want to implement updating a scheduled vaccination
    // fun updateScheduledVaccination(vaccineId: Long, scheduledVaccination: ScheduledVaccinationGetRequest) {
    //     viewModelScope.launch {
    //         val jwtToken = tokenManager.getJwtToken()
    //         httpService.updateScheduledVaccination(jwtToken, vaccineId, scheduledVaccination)
    //     }
    // }
}