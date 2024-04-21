package com.example.vaccineapp.fragments


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAddScheduledVaccinationBinding
import com.example.vaccineapp.domain.ReminderPostRequest
import com.example.vaccineapp.viewmodel.AddScheduledVaccinationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.ZoneId

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
            viewModel.fetchrecommendedVaccines()
            initDropdownMenu()
            viewModel.fetchScheduledVaccinations()
        }

        binding.chooseVaccineTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.setChosenVaccineIndex(position)
        }


        binding.pickDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val date = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
                val dateString = date.toString()
                viewModel.setChosenDate(dateString)
                binding.pickDate.setText(dateString)
            }
            datePicker.show(childFragmentManager, "date_picker")
        }

        binding.pickTime.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = String.format("%02d", timePicker.hour)
                val minute = String.format("%02d", timePicker.minute)
                viewModel.setChosenTime("$hour:$minute")
                binding.pickTime.setText("$hour:$minute")
            }
            timePicker.show(childFragmentManager, "time_picker")
        }


        binding.btnAddReminder.setOnClickListener {
            val reminder = ReminderPostRequest()
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val date = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
                val dateString = date.toString()
                reminder.dateTime = dateString

                val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
                timePicker.addOnPositiveButtonClickListener {
                    val hour = String.format("%02d", timePicker.hour)
                    val minute = String.format("%02d", timePicker.minute)
                    reminder.dateTime += "T$hour:$minute"

                    viewModel.addReminder(reminder)
                    inflateReminder(reminder)
                }
                timePicker.show(childFragmentManager, "time_picker")
            }
            datePicker.show(childFragmentManager, "date_picker")
        }
    }

    private fun inflateReminder(reminder: ReminderPostRequest) {
        val newReminder = LayoutInflater.from(requireContext()).inflate(R.layout.item_reminder, binding.reminderLayout, false)
        binding.reminderLayout.addView(newReminder)
        initReminder(newReminder, reminder)
    }

    private fun initReminder(reminderView: View, reminder: ReminderPostRequest) {
        val deleteButton = reminderView.findViewById<Button>(R.id.btnDelete)
        deleteButton.setOnClickListener {
            removeReminder(reminderView, reminder)
        }
        val reminderDate = reminderView.findViewById<TextView>(R.id.tvReminder)
        reminderDate.text = reminder.dateTime
    }

    private fun removeReminder(reminderView: View, reminder: ReminderPostRequest) {
        binding.reminderLayout.removeView(reminderView)
        viewModel.removeReminder(reminder)
    }

    private fun initDropdownMenu() {
        val adapter = ArrayAdapter(requireContext(), R.layout.vaccine_menu_item, viewModel.getVaccineNames())
        binding.chooseVaccineTextView.setAdapter(adapter)
    }

}