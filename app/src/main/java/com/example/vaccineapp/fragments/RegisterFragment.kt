package com.example.vaccineapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.vaccineapp.R

class RegisterFragment : Fragment() {

    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var fNameEditText: EditText
    private lateinit var lNameEditText: EditText
    private lateinit var DOBEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailEditText = view.findViewById(R.id.etRegisterEmail)
        passwordEditText = view.findViewById(R.id.etRegisterPass1)
        fNameEditText = view.findViewById(R.id.etFirstName)
        lNameEditText = view.findViewById(R.id.etLastName)
        DOBEditText = view.findViewById(R.id.etDOB)

        return view
    }

    fun gatherUserInput() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val fname = fNameEditText.text.toString()
        val lname = lNameEditText.text.toString()
        val dob = DOBEditText.text.toString()
    }
}