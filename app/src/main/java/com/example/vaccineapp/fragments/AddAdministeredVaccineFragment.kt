package com.example.vaccineapp.fragments

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
import com.example.vaccineapp.viewmodel.AddAdministeredVaccinationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.Instant.ofEpochMilli
import java.time.ZoneId
import kotlin.math.log

class AddAdministeredVaccineFragment : Fragment() {
    private val viewModel: AddAdministeredVaccinationViewModel by viewModel()
    private var _binding: FragmentAddAdministeredVaccineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAdministeredVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initDropdownMenu()
        }

        binding.chooseVaccineTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.setChosenVaccineIndex(position)
        }


        binding.pickDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val date = ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
                val dateString = date.toString()
                Log.d("AddAdministeredVaccineFragment", "Date: $dateString")
                viewModel.setChosenDate(dateString)
                binding.pickDate.editText?.setText(dateString)
            }
            datePicker.show(childFragmentManager, "date_picker")
        }

        binding.pickHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = String.format("%02d", timePicker.hour)
                val minute = String.format("%02d", timePicker.minute)
                viewModel.setChosenTime("$hour:$minute")
                binding.pickHour.editText?.setText("$hour:$minute")
            }
            timePicker.show(childFragmentManager, "time_picker")
        }

        binding.pickDoseNumber.editText?.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                val doseNumber = it.toString().toInt()
                viewModel.setDoseNumber(doseNumber)
            }
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.postVaccination()
            findNavController().navigate(R.id.action_addAdministeredVaccineFragment_to_mainMenuFragment)
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