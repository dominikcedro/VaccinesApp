package com.example.vaccineapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vaccineapp.databinding.FragmentAddAdministeredVaccineBinding
import com.example.vaccineapp.viewmodel.AddAdministeredVaccinationViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAdministeredVaccineFragment : Fragment() {
    private var _binding: FragmentAddAdministeredVaccineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddAdministeredVaccinationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAdministeredVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initDropdownMenu()
        }
    }

    private suspend fun initDropdownMenu() {
        viewModel.fetchVaccines()
        val vaccineNames = viewModel.getVaccineNames()
        val vaccineAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vaccineNames)
        vaccineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.chooseVaccineTextView.setAdapter(vaccineAdapter)
    }
}