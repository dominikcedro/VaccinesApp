package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.Vaccine
import com.example.vaccineapp.service.TokenManager
import kotlinx.coroutines.launch
class UserVaccinesViewModel(private val httpService: HttpService, private val tokenManager: TokenManager) : ViewModel() {
    private val _scheduledVaccines = MutableLiveData<List<ScheduledVaccinationGetRequest>>()
    val scheduledVaccines: LiveData<List<ScheduledVaccinationGetRequest>> get() = _scheduledVaccines

    fun fetchScheduledVaccinesForUser(userId: Long) {
        viewModelScope.launch {
            try {
                val vaccines = httpService.getScheduledVaccinesForUser(tokenManager.getJwtToken() ?: "", userId )
                _scheduledVaccines.postValue(vaccines)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}