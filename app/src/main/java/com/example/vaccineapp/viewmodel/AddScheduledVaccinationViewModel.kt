package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.ScheduledVaccinationPostRequest
import com.example.vaccineapp.domain.Vaccine
import kotlinx.coroutines.launch

class AddScheduledVaccinationViewModel(private val httpService: HttpService) : ViewModel() {
    private var headerText = "Schedule first dose"
    private val reminders = mutableListOf<ReminderPostRequest>()
    private val recommendedVaccines = mutableListOf<Vaccine>()
    private val vaccines = mutableListOf<Vaccine>()
    private val alreadyScheduledVaccinations = mutableListOf<ScheduledVaccinationGetRequest>()
    private val chosenVaccineIndex = MutableLiveData<Int>()
    private val chosenTime = MutableLiveData<String>()
    private val chosenDate = MutableLiveData<String>()

    suspend fun fetchRecommendedVaccines() {
        recommendedVaccines.clear()
        val fetchedVaccines = httpService.fetchRecommendedVaccines()
        recommendedVaccines.addAll(fetchedVaccines)
        vaccines.clear()
        vaccines.addAll(fetchedVaccines)
    }

    suspend fun fetchScheduledVaccinations() {
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

    fun addReminder(reminder: ReminderPostRequest) {
        this.reminders.add(reminder)
    }

    fun postVaccination(){
        viewModelScope.launch{
            val vaccine = vaccines[chosenVaccineIndex.value!!].id
            val doseNumber = 1
            val dateTime = chosenDate.value!! + "T" + chosenTime.value!! + ":00"
            val scheduledVaccination = ScheduledVaccinationPostRequest(vaccine, doseNumber, dateTime,reminders )
            httpService.postScheduledVaccination(scheduledVaccination)
        }
    }
}