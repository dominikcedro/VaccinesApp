package com.example.vaccineapp.viewmodel

import HttpService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.auth.RegistrationRequest
import com.example.vaccineapp.service.TokenManager
import com.example.vaccineapp.service.Validator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel for user registration.
 *
 * @property tokenManager The token manager for handling user tokens.
 * @property validator The validator for validating user input.
 * @property httpService The HTTP service for making network requests.
 */
class RegisterViewModel(private val tokenManager: TokenManager,
                        private val validator: Validator,
                        private val httpService: HttpService
): ViewModel() {

    private val email = MutableLiveData<String>("")
    private val password = MutableLiveData<String>("")
    private val confirmPassword = MutableLiveData<String>("")
    private val firstName = MutableLiveData<String>("")
    private val lastName = MutableLiveData<String>("")
    private val DoB = MutableLiveData<String>("")
    val isAllFieldsValid = MutableLiveData<Boolean>(false)
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
        checkFormValidity()
    }

    /**
     * Updates the password value.
     *
     * @param newPassword The new password value.
     */
    fun updatePassword(newPassword: String) {
        password.value = newPassword
        checkFormValidity()
    }

    /**
     * Updates the confirm password value.
     *
     * @param newConfirmPassword The new confirm password value.
     */
    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
        checkFormValidity()
    }

    /**
     * Updates the first name value.
     *
     * @param newFirstName The new first name value.
     */
    fun updateFirstName(newFirstName: String) {
        firstName.value = newFirstName
        checkFormValidity()
    }

    /**
     * Updates the last name value.
     *
     * @param newLastName The new last name value.
     */
    fun updateLastName(newLastName: String) {
        lastName.value = newLastName
        checkFormValidity()
    }

    /**
     * Sets the date of birth value.
     *
     * @param newDoB The new last DoB value.
     */
    fun setDoB(newDoB: String) {
        DoB.value = newDoB
        checkFormValidity()
    }

    /**
     * Checks if the email is valid.
     *
     * @return True if the email is valid, false otherwise.
     */
    fun isEmailValid(): Boolean {
        return validator.isValidEmail(email.value ?: "")
    }

    /**
     * Checks if the password is valid.
     *
     * @return True if the password is valid, false otherwise.
     */
    fun isPasswordValid(): Boolean {
        return validator.isValidPassword(password.value ?: "")
    }

    /**
     * Checks if the first name is valid.
     *
     * @return True if the first name is valid, false otherwise.
     */
    fun isFirstNameValid(): Boolean {
        return validator.isNotBlank(firstName.value ?: "")
    }

    /**
     * Checks if the last name is valid.
     *
     * @return True if the last name is valid, false otherwise.
     */
    fun isLastNameValid(): Boolean {
        return validator.isNotBlank(lastName.value ?: "")
    }

    /**
     * Checks if the confirm password is valid.
     *
     * @return True if the confirm password is valid, false otherwise.
     */
    fun isConfirmPasswordValid(): Boolean {
        return validator.isValidConfirmPassword(password.value ?: "", confirmPassword.value ?: "")
    }
    /**
     * Checks if the date of birth is valid.
     *
     * @return True if the date of birth is valid, false otherwise.
     */
    fun isDoBValid(): Boolean {
        return validator.isNotBlank(DoB.value ?: "")
    }

    /**
     * Checks the form validity.
     */
    private fun checkFormValidity() {
        isAllFieldsValid.value = isFirstNameValid() &&
                isLastNameValid() &&
                isEmailValid() &&
                isPasswordValid() &&
                isConfirmPasswordValid() &&
                isDoBValid()
    }

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
     * Registers the user.
     */
    fun register(){
        viewModelScope.launch {
            authenticationState.value = AuthenticationState.LOADING
            try {
                val registrationRequest = RegistrationRequest(email.value ?: "", password.value ?: "",
                    firstName.value ?: "", lastName.value ?: "", DoB.value ?: "")

                val tokensDTO = httpService.register(registrationRequest)

                tokenManager.saveTokens(tokensDTO.token, tokensDTO.refreshToken, tokensDTO.expirationDate)
                authenticationState.value = AuthenticationState.AUTHENTICATED
            } catch (e: Exception) {
                exceptionMessage.postValue(e.message)
                authenticationState.value = AuthenticationState.FAILED
            }
        }
    }
}