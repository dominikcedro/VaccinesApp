package com.example.vaccineapp.fragments

import HttpService
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.auth.AuthenticationRequest
import com.google.android.datatransport.runtime.logging.Logging
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger





import io.ktor.client.plugins.json.JsonPlugin
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.client.plugins.logging.DEFAULT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var httpService: HttpService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.etLoginEmail)
        val passwordEditText = view.findViewById<EditText>(R.id.etLoginPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)

        val noAuthHttpClient = HttpClient(Android) {
            install(JsonPlugin) {
                serializer = KotlinxSerializer()
            }
        }

        val defaultHttpClient = HttpClient(Android) {
            install(JsonPlugin) {
                serializer = KotlinxSerializer()
            }
            // Here you can add an interceptor to add the authentication token to each request
        }
        httpService = HttpService(noAuthHttpClient, defaultHttpClient)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val authenticationRequest = AuthenticationRequest(email, password)

            CoroutineScope(Dispatchers.IO).launch {
                val response = httpService.login(authenticationRequest)

                // Get the token, refreshToken, and expirationDate from the response
                val token = response.token
                val refreshToken = response.refreshToken
                val expirationDate = response.expirationDate

                // Store these values somewhere safe
                // For example, in shared preferences
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@launch
                with(sharedPref.edit()) {
                    putString("token", token)
                    putString("refreshToken", refreshToken)
                    putString("expirationDate", expirationDate)
                    apply()
                }

                // Navigate to another screen
                // For example, to a HomeFragment
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_loginFragment_to_mainMenuFragment)
                }
            }
        }
    }
}
