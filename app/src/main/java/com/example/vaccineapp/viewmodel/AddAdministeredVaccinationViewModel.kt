package com.example.vaccineapp.viewmodel

import HttpService
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest
import com.example.vaccineapp.domain.AdministeredVaccinePostRequest
import com.example.vaccineapp.domain.Vaccine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddAdministeredVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    private val vaccines = mutableListOf<Vaccine>()
    private val alreadyAdministeredVaccinations = mutableListOf<AdministeredVaccinationGetRequest>()
    private val chosenVaccineIndex = MutableLiveData<Int>()
    private val chosenDate = MutableLiveData<LocalDate>()
    private val chosenTime = MutableLiveData<LocalTime>()

    private val doseNumber = MutableLiveData<Int>()

    suspend fun fetchVaccines() {
        vaccines.clear()
        vaccines.addAll(httpService.fetchVaccines())
    }

    suspend fun fetchAdministeredVaccinations() {
        alreadyAdministeredVaccinations.clear()
        alreadyAdministeredVaccinations.addAll(httpService.fetchAdministeredVaccines())
    }

    fun getVaccineNames(): List<String> {
        return vaccines.map { it.name }
    }

    fun setChosenVaccineIndex(index: Int) {
        chosenVaccineIndex.value = index
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setChosenDate(date: String) {
        chosenDate.value = LocalDate.parse(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setChosenTime(time: String) {
        chosenTime.value = LocalTime.parse(time)
    }

    fun setDoseNumber(number: Int) {
        doseNumber.value = number
    }

    fun isVaccinationNonExisting(): Boolean {
        return alreadyAdministeredVaccinations.none {
            it.vaccine.id == vaccines[chosenVaccineIndex.value!!].id &&
            it.doseNumber == doseNumber.value!!
        }
    }
    fun areAllFieldsValid(): Boolean {
        return chosenVaccineIndex.value != null && chosenTime.value != null && chosenDate.value != null && doseNumber.value != null
    }

    fun isDoseNumberValidForChosenVaccine(): Boolean {
        return doseNumber.value!! <= vaccines[chosenVaccineIndex.value!!].doses.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postVaccination() {
        viewModelScope.launch {
            val vaccineId = vaccines[chosenVaccineIndex.value!!].id
            val doseNumber = doseNumber.value!!

            // Create a ZonedDateTime object from the chosen date and time
            val dateTime = LocalDateTime.of(chosenDate.value, chosenTime.value).atZone(ZoneId.systemDefault())

            // Format the ZonedDateTime object to a string
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            val formattedDateTime = dateTime.format(dateTimeFormatter)

            val administeredVaccine = AdministeredVaccinePostRequest(vaccineId, doseNumber, formattedDateTime)
            httpService.addAdministeredVaccine(administeredVaccine)
        }
    }
}