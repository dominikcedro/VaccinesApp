package com.example.vaccineapp.viewmodel

import HttpService
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest
import com.example.vaccineapp.domain.Reminder
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.ScheduledVaccinationPostRequest
import com.example.vaccineapp.domain.Vaccine
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AddScheduledVaccinationViewModel(private val httpService: HttpService) : ViewModel() {
    public var headerText = MutableLiveData<String>("Schedule dose number 1")
    private val reminders = mutableListOf<Reminder>()
    private val recommendedVaccines = mutableListOf<Vaccine>()
    private val alreadyScheduledVaccinations = mutableListOf<ScheduledVaccinationGetRequest>()
    private val chosenVaccineIndex = MutableLiveData<Int>()
    private var doseNumber = MutableLiveData<Int>(1)
    private val chosenZonedDateTime = MutableLiveData<ZonedDateTime?>()
    private var previousDoseZonedDateTime: ZonedDateTime? = null

    suspend fun fetchRecommendedVaccines() {
        recommendedVaccines.clear()
        recommendedVaccines.addAll(httpService.fetchRecommendedVaccines())
    }

    suspend fun fetchScheduledVaccinations() {
        alreadyScheduledVaccinations.clear()
        alreadyScheduledVaccinations.addAll(httpService.fetchScheduledVaccines())
    }

    fun getVaccineNames(): List<String> {

        return recommendedVaccines.map { it.name }
    }

    fun setChosenVaccineIndex(index: Int) {
        chosenVaccineIndex.value = index
    }

    fun setChosenZonedDateTime(zonedDateTime: ZonedDateTime) {
        chosenZonedDateTime.value = zonedDateTime
    }

    fun addReminder(reminder: Reminder) {
        this.reminders.add(reminder)
    }

    fun removeReminder(reminder: Reminder) {
        this.reminders.remove(reminder)
    }

    fun addNextDose(){
        doseNumber.value = doseNumber.value!! + 1
        headerText.value = "Schedule dose number ${doseNumber.value}"
        reminders.clear()
        previousDoseZonedDateTime = chosenZonedDateTime.value
        chosenZonedDateTime.value = null
    }
    fun isThereNextDose(): Boolean {
        return doseNumber.value!! < recommendedVaccines[chosenVaccineIndex.value!!].doses.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRecommendedDateForDose(): ZonedDateTime? {
        val months = recommendedVaccines[chosenVaccineIndex.value!!].doses[doseNumber.value!!-1].monthsAfterPreviousDose
        return months?.toLong()?.let { previousDoseZonedDateTime!!.plusMonths(it) }
    }

    fun getCurrentDoseNumber (): Int {
        return doseNumber.value!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun postScheduledVaccination() {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        val reminderPostRequests = mutableListOf<ReminderPostRequest>()
        reminders.forEach {
            reminderPostRequests.add(ReminderPostRequest(it.dateTime!!.format(dateTimeFormatter)))
        }

        val vaccineId = recommendedVaccines[chosenVaccineIndex.value!!].id
        val doseNumber = doseNumber.value!!
        val dateTime = chosenZonedDateTime.value!!
        val scheduledVaccine = ScheduledVaccinationPostRequest(vaccineId, doseNumber, dateTime.format(dateTimeFormatter), reminderPostRequests)

        httpService.postScheduledVaccination(scheduledVaccine)
    }
}