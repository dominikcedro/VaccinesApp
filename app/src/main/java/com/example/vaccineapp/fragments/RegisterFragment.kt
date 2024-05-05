package com.example.vaccineapp.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.activity.MainActivity
import com.example.vaccineapp.databinding.FragmentRegisterBinding
import com.example.vaccineapp.utils.hideKeyboard
import com.example.vaccineapp.utils.showSnackBar
import com.example.vaccineapp.viewmodel.RegisterViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Fragment for registering a new user.
 */
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.etFirstName.addTextChangedListener { text ->
            viewModel.updateFirstName(text.toString())
            if (!viewModel.isFirstNameValid()) {
                //TODO
            }
        }

        binding.etLastName.addTextChangedListener { text ->
            viewModel.updateLastName(text.toString())
            if (!viewModel.isLastNameValid()) {
                //TODO
            }
        }

        binding.etRegisterEmail.addTextChangedListener { text ->
            viewModel.updateEmail(text.toString())
            if (!viewModel.isEmailValid()) {
                //TODO
            }
        }

        binding.etRegisterPass1.addTextChangedListener { text ->
            viewModel.updatePassword(text.toString())
            if (!viewModel.isPasswordValid()) {
                //TODO
            }
        }

        binding.etRegisterPass2.addTextChangedListener { text ->
            viewModel.updateConfirmPassword(text.toString())
            if (!viewModel.isConfirmPasswordValid()) {
                //TODO
            }
        }

        binding.etDOB.setOnClickListener {
            getSelectedDate()
        }


        viewModel.isAllFieldsValid.observe(viewLifecycleOwner) { isValid ->
            binding.registerButton.isEnabled = isValid
            binding.registerButton.isEnabled = true

        }

        binding.registerButton.setOnClickListener {
            viewModel.register()
            hideKeyboard(it)
        }
        binding.tvGoLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        viewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                RegisterViewModel.AuthenticationState.LOADING -> {
                    binding.registerButton.isEnabled = false
                }

                RegisterViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.registerButton.isEnabled = true // why???
                    // TODO snackbar and
                    showSnackBar("Register successful!", false)

                    //  viewModel.updateNotificationToken()????
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

                RegisterViewModel.AuthenticationState.FAILED -> {
                    binding.registerButton.isEnabled = true
                    showSnackBar("Register not successful!", true)

                }
                null -> {
                    binding.registerButton.isEnabled = true
                }
            }
        }
        viewModel.exceptionMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                showSnackBar(message, true)
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDate(){
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        val constraintsBuilder = CalendarConstraints.Builder()

        val today = LocalDate.now(ZoneId.systemDefault())
        val startDate = today.minusYears(100)
        val endDate = today.minusYears(18)

        val startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        constraintsBuilder.setStart(startMillis)
        constraintsBuilder.setEnd(endMillis)
        constraintsBuilder.setOpenAt(endMillis)

        val constraints = constraintsBuilder.build()
        datePickerBuilder.setCalendarConstraints(constraints)

        val datePicker = datePickerBuilder.build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val date = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            binding.etDOB.setText(date.format(formatter))
            viewModel.setDoB(date.format(formatter))
        }
        datePicker.show(childFragmentManager, "date_picker")
    }
    }