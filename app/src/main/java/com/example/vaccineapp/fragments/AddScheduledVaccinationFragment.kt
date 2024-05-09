package com.example.vaccineapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAddScheduledVaccinationBinding
import com.example.vaccineapp.domain.Reminder
import com.example.vaccineapp.utils.showSnackBar
import com.example.vaccineapp.viewmodel.AddScheduledVaccinationViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime

/**
 * Fragment for adding scheduled vaccinations.
 */
class AddScheduledVaccinationFragment : Fragment() {
    private val viewModel: AddScheduledVaccinationViewModel by viewModel()
    private var _binding: FragmentAddScheduledVaccinationBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddScheduledVaccinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearReminders()

        binding.btnSubmit.isEnabled = false

        lifecycleScope.launch {
            viewModel.fetchRecommendedVaccines()
            initDropdownMenu()
            viewModel.fetchScheduledVaccinations()
        }

        binding.chooseVaccineTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.setChosenVaccineIndex(position)
        }

        viewModel.headerText.observe(viewLifecycleOwner) {
            binding.tvWelcome.text = it
        }

        viewModel.chosenZonedDateTime.observe(viewLifecycleOwner) {
            binding.btnSubmit.isEnabled = viewModel.areAllFieldsValid()
        }

        viewModel.chosenVaccineIndex.observe(viewLifecycleOwner) {
            binding.btnSubmit.isEnabled = viewModel.areAllFieldsValid()
        }

        binding.pickDate.setOnClickListener {
            getSelectedDateTime()
        }

        binding.btnAddReminder.setOnClickListener {
            if (viewModel.getReminders().size >= 3) {
                showSnackBar("You can only have 3 reminders", true)
                return@setOnClickListener
            }
            if (viewModel.chosenZonedDateTime.value == null) {
                showSnackBar("Please choose a vaccination date first", true)
                return@setOnClickListener
            }
            val reminder = Reminder()
            setDateTimeForReminder(reminder)
        }

        binding.btnSubmit.setOnClickListener {
            if (!viewModel.isVaccinationNonExisting()){
                showSnackBar("Vaccination already exists", true)
                return@setOnClickListener
            }
            if (viewModel.isThereNextDose()) {
                lifecycleScope.launch {
                    viewModel.postScheduledVaccination()
                    viewModel.addNextDose()
                    binding.pickDate.setText("")
                    binding.reminderLayout.removeAllViews()
                    binding.chooseVaccineTextView.isEnabled = false
                }
            } else {
                lifecycleScope.launch {
                    viewModel.postScheduledVaccination()
                    findNavController().navigate(R.id.action_addScheduledVaccinationFragment_to_mainMenuFragment)
                }
            }
        }
    }

    /**
     * Get the selected date and time.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDateTime(){
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setStart(MaterialDatePicker.todayInUtcMilliseconds())

        if (viewModel.getCurrentDoseNumber() > 1){
            val oneWeek = Period.ofWeeks(1)
            val recommendedDate = viewModel.getRecommendedDateForDose()

            val startDate = recommendedDate!!.minus(oneWeek).toInstant().toEpochMilli()
            val endDate = recommendedDate.plus(oneWeek).toInstant().toEpochMilli()

            constraintsBuilder
                .setStart(maxOf(startDate, MaterialDatePicker.todayInUtcMilliseconds()))
                .setEnd(endDate)
        }

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
                    .setTheme(R.style.TimePickerTheme) // theme fr timepicker TODO check what fails to set proper colors
                    .build()
                timePicker.addOnPositiveButtonClickListener {
                    val hour = timePicker.hour
                    val minute = timePicker.minute
                    val localTime = LocalTime.of(hour, minute)
                    val localDateTime = LocalDateTime.of(date, localTime)

                    val zoneOffset = OffsetDateTime.now(ZoneId.systemDefault()).offset
                    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(zoneOffset.id))

                    viewModel.setChosenZonedDateTime(zonedDateTime)
                    val formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")

                    binding.pickDate.setText(zonedDateTime.format(formatter))
                }
                timePicker.show(childFragmentManager, "time_picker")
            }
        }
        datePicker.show(childFragmentManager, "date_picker")
    }

    /**
     * Set the date and time for the reminder.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateTimeForReminder(reminder: Reminder) {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val date = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()

            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                val localTime = LocalTime.of(hour, minute)
                val localDateTime = LocalDateTime.of(date, localTime)

                // Get the system's zone offset
                val zoneOffset = OffsetDateTime.now(ZoneId.systemDefault()).offset
                val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(zoneOffset.id))

                reminder.dateTime = zonedDateTime

                viewModel.addReminder(reminder)
                inflateReminder(reminder)
            }
            timePicker.show(childFragmentManager, "time_picker")
        }
        datePicker.show(childFragmentManager, "date_picker")
    }

    /**
     * Inflate the reminder.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun inflateReminder(reminder: Reminder) {
        val newReminder = LayoutInflater.from(requireContext()).inflate(R.layout.item_reminder, binding.reminderLayout, false)
        binding.reminderLayout.addView(newReminder)
        initReminder(newReminder, reminder)
    }

    /**
     * Initialize the reminder.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initReminder(reminderView: View, reminder: Reminder) {
        val deleteButton = reminderView.findViewById<Button>(R.id.btnDelete)
        deleteButton.setOnClickListener {
            removeReminder(reminderView, reminder)
        }
        val reminderDate = reminderView.findViewById<TextView>(R.id.tvReminder)
        val formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")
        reminderDate.text = reminder.dateTime?.format(formatter)
    }

    /**
     * Remove the reminder.
     */
    private fun removeReminder(reminderView: View, reminder: Reminder) {
        binding.reminderLayout.removeView(reminderView)
        viewModel.removeReminder(reminder)
    }

    /**
     * Initialize the dropdown menu.
     */
    private fun initDropdownMenu() {
        val adapter = ArrayAdapter(requireContext(), R.layout.vaccine_menu_item, viewModel.getVaccineNames())
        binding.chooseVaccineTextView.setAdapter(adapter)
    }
}