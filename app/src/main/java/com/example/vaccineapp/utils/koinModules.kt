package com.example.vaccineapp.utils

import HttpService

import android.content.Context
import android.content.SharedPreferences
import com.example.vaccineapp.service.MyFirebaseMessagingService
import com.example.vaccineapp.service.TokenManager
import com.example.vaccineapp.service.Validator
import com.example.vaccineapp.viewmodel.LoginViewModel
import com.example.vaccineapp.viewmodel.RegisterViewModel
import com.example.vaccineapp.auth.ErrorResponse
import com.example.vaccineapp.viewmodel.AddAdministeredVaccinationViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import org.koin.dsl.module
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named




val httpClientModule = module {
    single(named("defaultHttpClient")) {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
            install(Auth) {
                bearer {
                    val tokenManager: TokenManager = get()
                    loadTokens {
                        BearerTokens(
                            accessToken = tokenManager.getJwtToken() ?: "",
                            refreshToken = tokenManager.getRefreshToken() ?: ""
                        )
                    }
                    refreshTokens {
                        tokenManager.refreshTokens()
                        BearerTokens(
                            accessToken = tokenManager.getJwtToken() ?: "",
                            refreshToken = tokenManager.getRefreshToken() ?: ""
                        )
                    }

                }
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    single(named("noAuthHttpClient")) {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            expectSuccess = true
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, request ->
                    val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response
                    when (exceptionResponse.status) {
                        HttpStatusCode.Unauthorized -> {
                            val errorDetails = exceptionResponse.body<ErrorResponse>()
                            when(errorDetails.detail) {
                                "User not found" -> throw Exception("User not found")
                                "Bad credentials" -> throw Exception("Password incorrect")
                                else -> throw Exception("Unknown error")
                            }
                        }
                        HttpStatusCode.Conflict -> {
                            val errorDetails = exceptionResponse.body<ErrorResponse>()
                            when(errorDetails.detail) {
                                "User already exists" -> throw Exception("User already exists")
                                else -> throw Exception("Unknown error")
                            }
                        }
                    }
                }
            }

        }
    }
}

val serviceModule = module {
    single { TokenManager(get(named("noAuthHttpClient")), get())}
    single { Validator() }
    single { HttpService(get(named("noAuthHttpClient")), get(named("defaultHttpClient"))) }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("com.mwdziak.fitness_mobile_client", Context.MODE_PRIVATE)
    }
    single { MyFirebaseMessagingService() }
}

val viewModelModule = module {
    single { LoginViewModel(get(), get()) }
    single { RegisterViewModel(get(), get(), get()) }
    single { AddAdministeredVaccinationViewModel(get()) }
}
