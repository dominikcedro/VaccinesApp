package com.example.vaccineapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.vaccineapp.R

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailEditText = view.findViewById(R.id.etLoginEmail)
        passwordEditText = view.findViewById(R.id.etLoginPassword)

        return view
    }
    fun gatherUserInput() {
        val username = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

    }
}