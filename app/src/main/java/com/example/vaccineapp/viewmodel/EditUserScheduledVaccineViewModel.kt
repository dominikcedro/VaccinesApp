package com.example.vaccineapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import HttpService
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.ScheduledVaccinationPostRequest
import com.example.vaccineapp.service.TokenManager
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EditUserScheduledVaccineViewModel(private val tokenManager: TokenManager, private val httpService: HttpService) : ViewModel() {
    private val _scheduledVaccination = MutableLiveData<ScheduledVaccinationGetRequest?>()
    val scheduledVaccination: LiveData<ScheduledVaccinationGetRequest?> = _scheduledVaccination

    fun fetchScheduledVaccination(vaccineId: Long) {
        viewModelScope.launch {
            _scheduledVaccination.value = httpService.getSingleScheduledVaccination(tokenManager.getJwtToken() ?: "", vaccineId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateScheduledVaccination(vaccineId: Long, oldVaccineInfo: ScheduledVaccinationGetRequest, newDate: ZonedDateTime) {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        val reminderPostRequests = oldVaccineInfo.reminders.map { reminderGetRequest ->
            ReminderPostRequest(dateTime = reminderGetRequest.dateTime)
        }.toMutableList()
        val updatedVaccine = ScheduledVaccinationPostRequest(
            vaccineId = vaccineId,
            doseNumber = oldVaccineInfo.doseNumber,
            dateTime = newDate.format(dateTimeFormatter),
            reminders = reminderPostRequests
        )

        httpService.updateScheduledVaccination(updatedVaccine, vaccineId)
    }
}