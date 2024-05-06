package com.example.vaccineapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.auth.LogoutRequest
import HttpService
import com.example.vaccineapp.service.TokenManager
import kotlinx.coroutines.launch

/**
 * ViewModel for user settings.
 *
 * @property tokenManager The token manager for handling user tokens.
 * @property httpService The HTTP service for making network requests.
 */
class SettingsViewModel(private val tokenManager: TokenManager, private val httpService: HttpService) : ViewModel() {
    val logoutStatus = MutableLiveData<Boolean>()

    /**
     * Logs out the user.
     */
    fun logout() {
        viewModelScope.launch {
            try {
                val logoutRequest = LogoutRequest(tokenManager.getJwtToken() ?: "", tokenManager.getRefreshToken() ?: "")
                httpService.logout(logoutRequest)
                tokenManager.deleteTokens()
                logoutStatus.postValue(true)
            } catch (e: Exception) {
                logoutStatus.postValue(false)
            }
        }
    }
}