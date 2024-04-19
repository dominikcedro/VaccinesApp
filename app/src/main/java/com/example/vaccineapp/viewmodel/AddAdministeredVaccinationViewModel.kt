package com.example.vaccineapp.viewmodel

import HttpService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.Vaccine
import kotlinx.coroutines.launch

class AddAdministeredVaccinationViewModel(private val httpService: HttpService): ViewModel() {
    private val vaccines = mutableListOf<Vaccine>()


    suspend fun fetchVaccines() {
        vaccines.clear()
        vaccines.addAll(httpService.fetchVaccines())
    }

    fun getVaccineNames(): List<String> {
        return vaccines.map { it.name }
    }
}