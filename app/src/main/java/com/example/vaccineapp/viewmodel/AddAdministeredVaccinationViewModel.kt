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

/**
 * ViewModel for adding administered vaccinations.
 *
 * @property httpService The HTTP service for making network requests.
 */
class AddAdministeredVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    private val vaccines = mutableListOf<Vaccine>()
    private val alreadyAdministeredVaccinations = mutableListOf<AdministeredVaccinationGetRequest>()
    private val chosenVaccineIndex = MutableLiveData<Int>()
    private val chosenDate = MutableLiveData<LocalDate>()
    private val chosenTime = MutableLiveData<LocalTime>()
    private val doseNumber = MutableLiveData<Int>()

    /**
     * Fetches the list of vaccines from the server.
     */
    suspend fun fetchVaccines() {
        vaccines.clear()
        vaccines.addAll(httpService.fetchVaccines())
    }

    /**
     * Fetches the list of already administered vaccinations from the server.
     */
    suspend fun fetchAdministeredVaccinations() {
        alreadyAdministeredVaccinations.clear()
        alreadyAdministeredVaccinations.addAll(httpService.fetchAdministeredVaccines())
    }

    /**
     * @return A list of vaccine names.
     */
    fun getVaccineNames(): List<String> {
        return vaccines.map { it.name }
    }

    /**
     * Sets the index of the chosen vaccine.
     *
     * @param index The index of the chosen vaccine.
     */
    fun setChosenVaccineIndex(index: Int) {
        chosenVaccineIndex.value = index
    }

    /**
     * Sets the chosen date.
     *
     * @param date The chosen date as a string.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setChosenDate(date: String) {
        chosenDate.value = LocalDate.parse(date)
    }

    /**
     * Sets the chosen time.
     *
     * @param time The chosen time as a string.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun setChosenTime(time: String) {
        chosenTime.value = LocalTime.parse(time)
    }

    /**
     * Sets the dose number.
     *
     * @param number The dose number.
     */
    fun setDoseNumber(number: Int) {
        doseNumber.value = number
    }

    /**
     * Checks if the vaccination is non-existing.
     *
     * @return True if the vaccination is non-existing, false otherwise.
     */
    fun isVaccinationNonExisting(): Boolean {
        val chosenVaccineId = vaccines[chosenVaccineIndex.value ?: return false].id
        return alreadyAdministeredVaccinations.none {
            it.vaccine.id == chosenVaccineId &&
                    it.doseNumber == doseNumber.value
        }
    }

    /**
     * Checks if all fields are valid.
     *
     * @return True if all fields are valid, false otherwise.
     */
    fun areAllFieldsValid(): Boolean {
        return chosenVaccineIndex.value != null && chosenTime.value != null && chosenDate.value != null && doseNumber.value != null
    }

    /**
     * Checks if the dose number is valid for the chosen vaccine.
     *
     * @return True if the dose number is valid, false otherwise.
     */
    fun isDoseNumberValidForChosenVaccine(): Boolean {
        val chosenVaccineDoses = vaccines[chosenVaccineIndex.value ?: return false].doses.size
        return doseNumber.value?.let { it <= chosenVaccineDoses } ?: false
    }

    /**
     * Posts the vaccination to the server.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun postVaccination() {
        val chosenVaccineId = vaccines[chosenVaccineIndex.value ?: return].id
        val chosenDoseNumber = doseNumber.value ?: return
        val chosenDateValue = chosenDate.value ?: return
        val chosenTimeValue = chosenTime.value ?: return

        viewModelScope.launch {
            val dateTime = LocalDateTime.of(chosenDateValue, chosenTimeValue).atZone(ZoneId.systemDefault())
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            val formattedDateTime = dateTime.format(dateTimeFormatter)

            val administeredVaccine = AdministeredVaccinePostRequest(chosenVaccineId, chosenDoseNumber, formattedDateTime)
            httpService.addAdministeredVaccine(administeredVaccine)
        }
    }
}