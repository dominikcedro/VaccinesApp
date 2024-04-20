package com.example.vaccineapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAddAdministeredVaccineBinding
import com.example.vaccineapp.databinding.FragmentAddScheduledVaccinationBinding
import com.example.vaccineapp.viewmodel.AddScheduledVaccinationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}