package com.example.vaccineapp.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAddAdministeredVaccineBinding
import com.example.vaccineapp.utils.showSnackBar
import com.example.vaccineapp.viewmodel.AddAdministeredVaccinationViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.Instant.ofEpochMilli
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.log

class AddAdministeredVaccineFragment : Fragment() {
    private val viewModel: AddAdministeredVaccinationViewModel by viewModel()
    private var _binding: FragmentAddAdministeredVaccineBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAdministeredVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initDropdownMenu()
            viewModel.fetchAdministeredVaccinations()
        }

        binding.chooseVaccineTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.setChosenVaccineIndex(position)
        }


        binding.pickDate.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setEnd(MaterialDatePicker.todayInUtcMilliseconds())

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val date = ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
                val today = LocalDate.now(ZoneId.systemDefault())
                if (date.isAfter(today)) {
                    showSnackBar("Future dates not allowed", true)
                } else {
                    val dateString = date.toString()
                    viewModel.setChosenDate(dateString)
                    binding.pickDate.setText(dateString)
                }
            }
            datePicker.show(childFragmentManager, "date_picker")
        }

        binding.pickTime.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = String.format("%02d", timePicker.hour)
                val minute = String.format("%02d", timePicker.minute)
                viewModel.setChosenTime("$hour:$minute")
                binding.pickTime.setText("$hour:$minute")
            }
            timePicker.show(childFragmentManager, "time_picker")
        }

        binding.doseNumber.addTextChangedListener { text ->
            if (text.toString().isNotEmpty()) {
                viewModel.setDoseNumber(text.toString().toInt())
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (!viewModel.areAllFieldsValid()) {
                showSnackBar("Please fill in all fields", true)
                return@setOnClickListener
            }

            if (!viewModel.isVaccinationNonExisting()) {
                showSnackBar("Vaccination already exists", true)
                return@setOnClickListener
            }

            if (!viewModel.isDoseNumberValidForChosenVaccine()) {
                showSnackBar("Invalid dose number", true)
                return@setOnClickListener
            }
            viewModel.postVaccination()
            findNavController().navigate(R.id.action_addAdministeredVaccineFragment_to_administeredVaccinationsFragment)
        }

    }

    private suspend fun initDropdownMenu() {
        viewModel.fetchVaccines()
        val vaccineNames = viewModel.getVaccineNames()
        val vaccineAdapter =
            ArrayAdapter(requireContext(), R.layout.vaccine_menu_item, vaccineNames)
        binding.chooseVaccineTextView.setAdapter(vaccineAdapter)
    }
}