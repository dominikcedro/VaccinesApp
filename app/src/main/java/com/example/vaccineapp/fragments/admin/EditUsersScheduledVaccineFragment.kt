package com.example.vaccineapp.fragments.admin

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentEditUsersScheduledVaccineBinding
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.utils.showSnackBar
import com.example.vaccineapp.viewmodel.EditUserScheduledVaccineViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EditUsersScheduledVaccineFragment : Fragment() {
    private var _binding: FragmentEditUsersScheduledVaccineBinding? = null
    private val binding get() = _binding!!
    private lateinit var vaccineviewmodel: EditUserScheduledVaccineViewModel
    var oldVaccineInfo: ScheduledVaccinationGetRequest? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUsersScheduledVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vaccineviewmodel = getViewModel()

        val vaccineId = arguments?.getLong("vaccineId")
        if (vaccineId != null) {
            vaccineviewmodel.fetchScheduledVaccination(vaccineId)
            vaccineviewmodel.scheduledVaccination.observe(viewLifecycleOwner, Observer { scheduledVaccination ->
                // Update your views here with the fetched data
                binding.vaccineName.setText(scheduledVaccination?.vaccine?.name)
                binding.vaccineDate.setText(scheduledVaccination?.dateTime?.let { formatDate(it) })

                oldVaccineInfo = scheduledVaccination

            })

            binding.updateButton.setOnClickListener {
                findNavController().navigate(R.id.action_editUsersScheduledVaccineFragment_to_adminDashboardFragment)
            }
            binding.vaccineDate.setOnClickListener {
                val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
                val constraintsBuilder = CalendarConstraints.Builder()
                constraintsBuilder.setStart(MaterialDatePicker.todayInUtcMilliseconds())

                val constraints = constraintsBuilder.build()
                datePickerBuilder.setCalendarConstraints(constraints)

                val datePicker = datePickerBuilder.build()

                datePicker.addOnPositiveButtonClickListener { selectedDate ->
                    val date = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    val today = LocalDate.now(ZoneId.systemDefault())
                    if (date.isBefore(today)) {
                        showSnackBar("Past dates not allowed", true)
                    } else {
                        val timePicker = MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setTheme(R.style.TimePickerTheme)
                            .build()
                        timePicker.addOnPositiveButtonClickListener {
                            val hour = timePicker.hour
                            val minute = timePicker.minute
                            val localTime = LocalTime.of(hour, minute)
                            val localDateTime = LocalDateTime.of(date, localTime)

                            val zoneOffset = OffsetDateTime.now(ZoneId.systemDefault()).offset
                            val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(zoneOffset.id))

                            // Call the ViewModel's method to update the scheduled vaccination
                            vaccineviewmodel.viewModelScope.launch {
                                try {
                                    // Replace 'vaccineId' and 'oldVaccineInfo' with the actual values
                                    oldVaccineInfo?.let { it1 ->
                                        vaccineviewmodel.updateScheduledVaccination(vaccineId,
                                            it1, zonedDateTime)
                                    }
                                    // Handle successful update
                                } catch (e: Exception) {
                                    // Handle error
                                }
                            }

                            val formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")
                            val zonedDateWithSystemOffset = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
                            binding.vaccineDate.setText(zonedDateWithSystemOffset.format(formatter))
                        }
                        timePicker.show(childFragmentManager, "time_picker")
                    }
                }
                datePicker.show(childFragmentManager, "date_picker")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date: String): String {
        val targetFormatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")
        val sourceFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        val zonedDate = ZonedDateTime.parse(date, sourceFormatter)
        val zonedDateWithSystemOffset = zonedDate.withZoneSameInstant(ZoneId.systemDefault())
        return zonedDateWithSystemOffset.format(targetFormatter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}