import com.example.vaccineapp.auth.AuthenticationRequest
import com.example.vaccineapp.auth.AuthenticationResponse
import com.example.vaccineapp.auth.NotificationTokenRequest
import com.example.vaccineapp.auth.RegistrationRequest
import com.example.vaccineapp.auth.LogoutRequest
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest
import com.example.vaccineapp.domain.AdministeredVaccinePostRequest
import com.example.vaccineapp.domain.Vaccine

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class HttpService(private val noAuthHttpClient: HttpClient, private val defaultHttpClient: HttpClient) {
    private val usersServiceUrl = "http://ec2-13-49-243-234.eu-north-1.compute.amazonaws.com"

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

    suspend fun updateNotificationToken(token: String) {
        val url = "$usersServiceUrl/user/notification-token"
        val response = defaultHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(NotificationTokenRequest(token))
        }
    }

    suspend fun fetchVaccines(): List<Vaccine> {
        val url = "$usersServiceUrl/vaccine/recommended"
        val response = defaultHttpClient.get(url)
        return response.body<List<Vaccine>>()
    }

    suspend fun addAdministeredVaccine(administeredVaccinePostRequest: AdministeredVaccinePostRequest) {
        val url = "$usersServiceUrl/vaccination/administered"
        defaultHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(administeredVaccinePostRequest)
        }
    }

    suspend fun fetchAdministeredVaccines(): List<AdministeredVaccinationGetRequest> {
        val url = "$usersServiceUrl/vaccination/administered/user"
        val response = defaultHttpClient.get(url)
        return response.body<List<AdministeredVaccinationGetRequest>>()
    }

}