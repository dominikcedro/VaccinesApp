package com.example.vaccineapp.fragments


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAddScheduledVaccinationBinding
import com.example.vaccineapp.domain.Reminder
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.viewmodel.AddScheduledVaccinationViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


        binding.pickDate.setOnClickListener {
            getSelectedDateTime()
        }

        binding.btnAddReminder.setOnClickListener {
            val reminder = Reminder()
            setDateTimeForReminder(reminder)
        }


        binding.btnSubmit.setOnClickListener {
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDateTime(){
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()


        if (viewModel.getCurrentDoseNumber() > 1){
            val oneWeek = Period.ofWeeks(1)
            val recommendedDate = viewModel.getRecommendedDateForDose()

            val startDate = recommendedDate!!.minus(oneWeek).toInstant().toEpochMilli()
            val endDate = recommendedDate.plus(oneWeek).toInstant().toEpochMilli()

            val constraintsBuilder = CalendarConstraints.Builder()
                .setStart(startDate)
                .setEnd(endDate)

            val constraints = constraintsBuilder.build()
            datePickerBuilder.setCalendarConstraints(constraints)

        }
        val datePicker = datePickerBuilder.build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val date = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()


            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
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
        datePicker.show(childFragmentManager, "date_picker")
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inflateReminder(reminder: Reminder) {
        val newReminder = LayoutInflater.from(requireContext()).inflate(R.layout.item_reminder, binding.reminderLayout, false)
        binding.reminderLayout.addView(newReminder)
        initReminder(newReminder, reminder)
    }

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

    private fun removeReminder(reminderView: View, reminder: Reminder) {
        binding.reminderLayout.removeView(reminderView)
        viewModel.removeReminder(reminder)
    }

    private fun initDropdownMenu() {
        val adapter = ArrayAdapter(requireContext(), R.layout.vaccine_menu_item, viewModel.getVaccineNames())
        binding.chooseVaccineTextView.setAdapter(adapter)
    }

}