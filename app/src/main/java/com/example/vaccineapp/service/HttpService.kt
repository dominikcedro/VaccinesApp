import com.example.vaccineapp.auth.AuthenticationRequest
import com.example.vaccineapp.auth.AuthenticationResponse
import com.example.vaccineapp.auth.RegistrationRequest
import com.mwdziak.fitness_mobile_client.auth.LogoutRequest

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HttpService(private val noAuthHttpClient: HttpClient, private val defaultHttpClient: HttpClient) {
    private val usersServiceUrl = "http://localhost:8080"

    suspend fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        val url = "$usersServiceUrl/auth/login"

        val response: HttpResponse = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(authenticationRequest)
        }

        return response.body<AuthenticationResponse>()
    }

    suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResponse {
        val url = "$usersServiceUrl/auth/register"

        val response: HttpResponse = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(registrationRequest)
        }

        return response.body<AuthenticationResponse>()
    }

    suspend fun logout(logoutRequest: LogoutRequest): HttpResponse {
        val url = "$usersServiceUrl/auth/logout"
        val response = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(logoutRequest)
        }
        return response
    }

}