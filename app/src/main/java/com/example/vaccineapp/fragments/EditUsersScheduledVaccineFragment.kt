package com.example.vaccineapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentEditUsersScheduledVaccineBinding
import com.example.vaccineapp.viewmodel.EditUserScheduledVaccineViewModel
import com.example.vaccineapp.viewmodel.UserVaccinesViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class EditUsersScheduledVaccineFragment : Fragment() {
    private var _binding: FragmentEditUsersScheduledVaccineBinding? = null
    private val binding get() = _binding!!
    private lateinit var vaccineviewmodel: EditUserScheduledVaccineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUsersScheduledVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vaccineviewmodel = getViewModel()

        val vaccineId = arguments?.getLong("vaccineId")
        if (vaccineId != null) {
            vaccineviewmodel.fetchScheduledVaccination(vaccineId)
            vaccineviewmodel.scheduledVaccination.observe(viewLifecycleOwner, Observer { scheduledVaccination ->
                // Update your views here with the fetched data
                binding.vaccineName.setText(scheduledVaccination?.vaccine?.name)
                binding.vaccineDate.setText(scheduledVaccination?.dateTime)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}