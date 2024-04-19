package com.example.vaccineapp.viewmodel

import HttpService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.AdministeredVaccinePostRequest
import com.example.vaccineapp.domain.Vaccine
import kotlinx.coroutines.launch

class AddAdministeredVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    private val vaccines = mutableListOf<Vaccine>()
    private val chosenVaccineIndex = MutableLiveData<Int>()
    private val chosenTime = MutableLiveData<String>()
    private val chosenDate = MutableLiveData<String>()
    private val doseNumber = MutableLiveData<Int>()

    suspend fun fetchVaccines() {
        vaccines.clear()
        vaccines.addAll(httpService.fetchVaccines())
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

    fun setDoseNumber(number: Int) {
        doseNumber.value = number
    }

    fun postVaccination() {
        viewModelScope.launch {
            val vaccineId = vaccines[chosenVaccineIndex.value!!].id
            val doseNumber = doseNumber.value!!
            val dateTime = chosenDate.value!! + " " + chosenTime.value!!
            val administeredVaccine = AdministeredVaccinePostRequest(vaccineId, doseNumber, dateTime)
            httpService.addAdministeredVaccine(administeredVaccine)
        }
    }
}