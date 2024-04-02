package com.example.vaccineapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentRegisterBinding
import com.example.vaccineapp.utils.hideKeyboard
import com.example.vaccineapp.utils.showSnackBar
import com.example.vaccineapp.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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


        viewModel.isAllFieldsValid.observe(viewLifecycleOwner) { isValid ->
            binding.registerButton.isEnabled = isValid
        }

        binding.registerButton.setOnClickListener {
            viewModel.register()
            hideKeyboard(it)
        }

        viewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                RegisterViewModel.AuthenticationState.LOADING -> {
                    binding.registerButton.isEnabled = false
                }

                RegisterViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.registerButton.isEnabled = true
                    //TODO
                }

                RegisterViewModel.AuthenticationState.FAILED -> {
                    binding.registerButton.isEnabled = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}