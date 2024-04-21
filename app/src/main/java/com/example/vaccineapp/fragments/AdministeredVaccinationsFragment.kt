package com.example.vaccineapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAddAdministeredVaccineBinding
import com.example.vaccineapp.databinding.FragmentAdministeredVaccinationsBinding
import com.example.vaccineapp.recycler_view.AdministeredVaccinationAdapter
import com.example.vaccineapp.viewmodel.AddAdministeredVaccinationViewModel
import com.example.vaccineapp.viewmodel.AdministeredVaccinationViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController

class AdministeredVaccinationsFragment : Fragment() {
    private val viewModel: AdministeredVaccinationViewModel by viewModel()
    private var _binding: FragmentAdministeredVaccinationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministeredVaccinationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            viewModel.getAdministeredVaccinations()
            val adapter = AdministeredVaccinationAdapter(viewModel.administeredVaccinations.toTypedArray()){
                // Navigate to the other fragment
                findNavController().navigate(R.id.action_administeredVaccinationsFragment_to_addAdministeredVaccineFragment)
            }
            binding.administeredVaccinationsRV.adapter = adapter
        }
        val layoutManager = LinearLayoutManager(context)
        binding.administeredVaccinationsRV.layoutManager = layoutManager
    }

}