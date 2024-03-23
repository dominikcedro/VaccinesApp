package com.example.vaccineapp.startup

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.MainActivity
import com.example.vaccineapp.R
import io.ktor.client.*
import io.ktor.client.engine.android.*

import kotlinx.serialization.json.Json

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        setupRegisterLink(view)

        val usernameEditText = view.findViewById<EditText>(R.id.etLoginEmail)
        val passwordEditText = view.findViewById<EditText>(R.id.etLoginPassword)


        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
        }

        return view
    }

    private fun setupRegisterLink(view: View) {
        val registerLinkTextView = view.findViewById<TextView>(R.id.tvGoRegister)
        val spannableRegisterLink = createSpannableRegisterLink()

        registerLinkTextView.text = spannableRegisterLink
        registerLinkTextView.movementMethod = LinkMovementMethod.getInstance()
    }
    private fun createSpannableRegisterLink(): SpannableString {
        val fullText = "Need help? Register here."
        val clickableWord = "Register here."
        val spannableRegisterLink = SpannableString(fullText)
        val startIndex = fullText.indexOf(clickableWord)

        val registerLinkSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
        spannableRegisterLink.setSpan(
            registerLinkSpan,
            startIndex,
            startIndex + clickableWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableRegisterLink
    }
}