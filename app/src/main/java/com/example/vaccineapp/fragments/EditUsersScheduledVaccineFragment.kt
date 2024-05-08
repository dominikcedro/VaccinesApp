package com.example.vaccineapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentEditUsersScheduledVaccineBinding
import com.example.vaccineapp.viewmodel.UserVaccinesViewModel

class EditUsersScheduledVaccineFragment : Fragment() {
    private var _binding: FragmentEditUsersScheduledVaccineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUsersScheduledVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}