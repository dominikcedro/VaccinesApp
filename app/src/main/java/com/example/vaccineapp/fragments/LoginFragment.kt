package com.example.vaccineapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.databinding.FragmentLoginBinding
import com.example.vaccineapp.utils.hideKeyboard
import com.example.vaccineapp.utils.showSnackBar
import com.mwdziak.fitness_mobile_client.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.vaccineapp.R

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModel()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (viewModel.isUserLoggedIn()) {
            //TODO
        }

        binding.tvGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.etLoginEmail.addTextChangedListener { text ->
            viewModel.updateEmail(text.toString())
        }

        binding.etLoginPassword.addTextChangedListener { text ->
            viewModel.updatePassword(text.toString())
        }

        binding.btnLogin.setOnClickListener {
            viewModel.authenticate()
            hideKeyboard(it)
        }

        viewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.LOADING -> {
                    binding.btnLogin.isEnabled = false
                }

                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.btnLogin.isEnabled = true
                    //TODO
                }

                LoginViewModel.AuthenticationState.FAILED -> {
                    binding.btnLogin.isEnabled = true
                }
                null -> {
                    binding.btnLogin.isEnabled = true
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
    companion object {
        fun newInstance() = LoginFragment()
    }
}