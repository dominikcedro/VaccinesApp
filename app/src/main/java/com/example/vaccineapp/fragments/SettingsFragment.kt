package com.example.vaccineapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.activity.MainActivity
import com.example.vaccineapp.activity.StartActivity
import com.example.vaccineapp.utils.showSnackBar
import com.example.vaccineapp.viewmodel.RegisterViewModel
import com.example.vaccineapp.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvFirstName: TextView = view.findViewById(R.id.tvFirstName)
        val tvLastName: TextView = view.findViewById(R.id.tvLastName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvDoB: TextView = view.findViewById(R.id.tvDOB)
        val tvRole: TextView = view.findViewById(R.id.tvRole)

        viewModel.getUserDetails()

        // Get the user details from the view model
        viewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            tvFirstName.text = "Name: ${userDetails.firstName}"
            tvLastName.text = "Last Name: ${userDetails.lastName}"
            tvEmail.text = "Email: ${userDetails.email}"
            tvDoB.text = "Date of Birth: ${userDetails.dateOfBirth}"
            tvRole.text = "Role: ${userDetails.role}"
        }



        val logoutButton: Button = view.findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            viewModel.logout()
        }

        viewModel.logoutStatus.observe(viewLifecycleOwner) { isLoggedOut ->
            if (isLoggedOut) {
                showSnackBar("Logout successful", false)
                activity?.finish()
            } else {
                showSnackBar("Logout failed", true)
            }
        }
    }
}