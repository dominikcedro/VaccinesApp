package com.example.vaccineapp.viewmodel

import HttpService
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vaccineapp.domain.Reminder
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.ScheduledVaccinationPostRequest
import com.example.vaccineapp.domain.Vaccine
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel for scheduling vaccinations.
 *
 * @property httpService The HTTP service for making network requests.
 */
class AddScheduledVaccinationViewModel(private val httpService: HttpService) : ViewModel() {
    var headerText = MutableLiveData<String>("Schedule dose number 1")
    private val reminders = mutableListOf<Reminder>()
    private val recommendedVaccines = mutableListOf<Vaccine>()
    private val alreadyScheduledVaccinations = mutableListOf<ScheduledVaccinationGetRequest>()
    val chosenVaccineIndex = MutableLiveData<Int>()
    private var doseNumber = MutableLiveData<Int>(1)
    val chosenZonedDateTime = MutableLiveData<ZonedDateTime?>()
    private var previousDoseZonedDateTime: ZonedDateTime? = null

    /**
     * Fetches the list of recommended vaccines from the server.
     */
    suspend fun fetchRecommendedVaccines() {
        recommendedVaccines.clear()
        recommendedVaccines.addAll(httpService.fetchRecommendedVaccines())
    }

    /**
     * Fetches the list of already scheduled vaccinations from the server.
     */
    suspend fun fetchScheduledVaccinations() {
        alreadyScheduledVaccinations.clear()
        alreadyScheduledVaccinations.addAll(httpService.fetchScheduledVaccines())
    }

    /**
     * @return A list of vaccine names.
     */
    fun getVaccineNames(): List<String> {
        return recommendedVaccines.map { it.name }
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
     * Sets the chosen date and time.
     *
     * @param zonedDateTime The chosen date and time.
     */
    fun setChosenZonedDateTime(zonedDateTime: ZonedDateTime) {
        chosenZonedDateTime.value = zonedDateTime
    }

    /**
     * Adds a reminder.
     *
     * @param reminder The reminder to add.
     */
    fun addReminder(reminder: Reminder) {
        this.reminders.add(reminder)
    }

    /**
     * Removes a reminder.
     *
     * @param reminder The reminder to remove.
     */
    fun removeReminder(reminder: Reminder) {
        this.reminders.remove(reminder)
    }

    /**
     * @return A list of reminders.
     */
    fun getReminders(): List<Reminder> {
        return reminders
    }

    /**
     * Adds the next dose.
     */
    fun addNextDose(){
        if (!isThereNextDose()) {
            return
        }
        doseNumber.value = doseNumber.value!! + 1
        headerText.value = "Schedule dose number ${doseNumber.value}"
        reminders.clear()
        previousDoseZonedDateTime = chosenZonedDateTime.value
        chosenZonedDateTime.value = null
    }

    /**
     * Checks if there is a next dose.
     *
     * @return True if there is a next dose, false otherwise.
     */
    fun isThereNextDose(): Boolean {
        return doseNumber.value!! < recommendedVaccines[chosenVaccineIndex.value!!].doses.size
    }

    /**
     * Gets the recommended date for the dose.
     *
     * @return The recommended date for the dose.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getRecommendedDateForDose(): ZonedDateTime? {
        val months = recommendedVaccines[chosenVaccineIndex.value!!].doses[doseNumber.value!!-1].monthsAfterPreviousDose
        return months?.toLong()?.let { previousDoseZonedDateTime!!.plusMonths(it) }
    }

    /**
     * @return The current dose number.
     */
    fun getCurrentDoseNumber (): Int {
        return doseNumber.value!!
    }

    /**
     * Checks if the vaccination is non-existing.
     *
     * @return True if the vaccination is non-existing, false otherwise.
     */
    fun isVaccinationNonExisting(): Boolean {
        return alreadyScheduledVaccinations.none {
            it.vaccine.id == recommendedVaccines[chosenVaccineIndex.value!!].id &&
                    it.doseNumber == doseNumber.value!!
        }
    }

    /**
     * Checks if all fields are valid.
     *
     * @return True if all fields are valid, false otherwise.
     */
    fun areAllFieldsValid(): Boolean {
        return chosenVaccineIndex.value != null && chosenZonedDateTime.value != null
    }

    /**
     * Posts the scheduled vaccination to the server.
     */
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