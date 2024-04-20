package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.Vaccine

class AddScheduledVaccinationViewModel(private val httpService: HttpService) : ViewModel() {
    private var headerText = "Schedule first dose"
    private val reminder = mutableListOf<ReminderPostRequest>()
    private val recommendedVaccines = mutableListOf<Vaccine>()
    private val vaccines = mutableListOf<Vaccine>()
    private val alreadyScheduledVaccinations = mutableListOf<ScheduledVaccinationGetRequest>()
    private val chosenVaccineIndex = MutableLiveData<Int>()
    private val chosenTime = MutableLiveData<String>()
    private val chosenDate = MutableLiveData<String>()

    suspend fun fetchrecommendedVaccines() {
        recommendedVaccines.clear()
        recommendedVaccines.addAll(httpService.fetchRecommendedVaccines())
    }

    suspend fun fetchAdministeredVaccinations() {
        alreadyScheduledVaccinations.clear()
        alreadyScheduledVaccinations.addAll(httpService.fetchScheduledVaccines())
    }

    fun getVaccineNames(): List<String> {
        return vaccines.map { it.name }
    }

    fun setChosenVaccineIndex(index: Int) {
        chosenVaccineIndex.value = index
    }

    fun setChosenTime(time: String) {
        chosenTime.value = time
    }
    fun setChosenDate(date: String) {
        chosenDate.value = date
    }
}