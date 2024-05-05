package com.example.vaccineapp.viewmodel

import HttpService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.auth.AuthenticationRequest
import com.example.vaccineapp.service.TokenManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel for user login.
 *
 * @property tokenManager The token manager for handling user tokens.
 * @property httpService The HTTP service for making network requests.
 */
class LoginViewModel(private val tokenManager: TokenManager, private val httpService: HttpService) : ViewModel() {
    private val email = MutableLiveData<String>("")
    private val password = MutableLiveData<String>("")
    val exceptionMessage = MutableLiveData<String>()

    /**
     * Represents the authentication state.
     */
    val authenticationState = MutableLiveData<AuthenticationState>()

    /**
     * Enum class for representing the authentication state.
     */
    enum class AuthenticationState {
        LOADING, AUTHENTICATED, FAILED
    }

    /**
     * Updates the email value.
     *
     * @param newEmail The new email value.
     */
    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    /**
     * Updates the password value.
     *
     * @param newPassword The new password value.
     */
    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    /**
     * Authenticates the user.
     */
    fun authenticate() {
        viewModelScope.launch {
            authenticationState.value = AuthenticationState.LOADING
            try {
                val authenticationRequest = AuthenticationRequest(email.value ?: "", password.value ?: "")
                val tokensDTO = httpService.authenticate(authenticationRequest)
                tokenManager.saveTokens(tokensDTO.token, tokensDTO.refreshToken, tokensDTO.expirationDate)
                authenticationState.value = AuthenticationState.AUTHENTICATED
            } catch (e: Exception) {
                exceptionMessage.postValue(e.message)
                authenticationState.value = AuthenticationState.FAILED
            }
        }
    }

    /**
     * Updates the notification token.
     */
    fun updateNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Login Fragment", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            runBlocking {
                httpService.updateNotificationToken(token)
            }
        })
    }

    /**
     * Checks if the user is logged in.
     *
     * @return True if the user is logged in, false otherwise.
     */
    fun isUserLoggedIn(): Boolean {
        return !tokenManager.isRefreshTokenExpired();
    }
}