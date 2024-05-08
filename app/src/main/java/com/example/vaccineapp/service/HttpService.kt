
import com.example.vaccineapp.auth.AuthenticationRequest
import com.example.vaccineapp.auth.AuthenticationResponse
import com.example.vaccineapp.auth.NotificationTokenRequest
import com.example.vaccineapp.auth.RegistrationRequest
import com.example.vaccineapp.auth.LogoutRequest
import com.example.vaccineapp.domain.AdministeredVaccinationGetRequest
import com.example.vaccineapp.domain.AdministeredVaccinePostRequest
import com.example.vaccineapp.domain.NewsArticle
import com.example.vaccineapp.domain.NewsResponse
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.domain.ScheduledVaccinationPostRequest
import com.example.vaccineapp.domain.UserDetails
import com.example.vaccineapp.domain.Vaccine

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Service for handling HTTP requests.
 *
 * @property noAuthHttpClient The HTTP client for making requests without authentication.
 * @property defaultHttpClient The HTTP client for making requests with authentication.
 */
class HttpService(private val noAuthHttpClient: HttpClient, private val defaultHttpClient: HttpClient) {
    private val usersServiceUrl = "http://ec2-13-49-243-234.eu-north-1.compute.amazonaws.com"

    /**
     * Authenticates the user.
     *
     * @param authenticationRequest The authentication request.
     * @return The authentication response.
     */
    suspend fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        val url = "$usersServiceUrl/auth/login"

        val response: HttpResponse = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(authenticationRequest)
        }

        return response.body<AuthenticationResponse>()
    }

    /**
     * Registers the user.
     *
     * @param registrationRequest The registration request.
     * @return The authentication response.
     */
    suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResponse {
        val url = "$usersServiceUrl/auth/register"

        val response: HttpResponse = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(registrationRequest)
        }

        return response.body<AuthenticationResponse>()
    }

    /**
     * Logs out the user.
     *
     * @param logoutRequest The logout request.
     * @return The HTTP response.
     */
    suspend fun logout(logoutRequest: LogoutRequest): HttpResponse {
        val url = "$usersServiceUrl/auth/logout"
        val response = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(logoutRequest)
        }
        return response
    }

    /**
     * Updates the notification token.
     *
     * @param token The notification token.
     */
    suspend fun updateNotificationToken(token: String) {
        val url = "$usersServiceUrl/user/notification-token"
        val response = defaultHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(NotificationTokenRequest(token))
        }
    }

    /**
     * Fetches the list of vaccines.
     *
     * @return The list of vaccines.
     */
    suspend fun fetchVaccines(): List<Vaccine> {
        val url = "$usersServiceUrl/vaccine"
        val response = defaultHttpClient.get(url)
        return response.body<List<Vaccine>>()
    }

    /**
     * Fetches the list of recommended vaccines.
     *
     * @return The list of recommended vaccines.
     */
    suspend fun fetchRecommendedVaccines(): List<Vaccine> {
        val url = "$usersServiceUrl/vaccine/recommended"
        val response = defaultHttpClient.get(url)
        return response.body<List<Vaccine>>()
    }

    /**
     * Adds an administered vaccine.
     *
     * @param administeredVaccinePostRequest The administered vaccine post request.
     */
    suspend fun addAdministeredVaccine(administeredVaccinePostRequest: AdministeredVaccinePostRequest) {
        val url = "$usersServiceUrl/vaccination/administered"
        defaultHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(administeredVaccinePostRequest)
        }
    }

    /**
     * Fetches the list of administered vaccines.
     *
     * @return The list of administered vaccines.
     */
    suspend fun fetchAdministeredVaccines(): List<AdministeredVaccinationGetRequest> {
        val url = "$usersServiceUrl/vaccination/administered/user"
        val response = defaultHttpClient.get(url)
        return response.body<List<AdministeredVaccinationGetRequest>>()
    }

    /**
     * Fetches the list of scheduled vaccines.
     *
     * @return The list of scheduled vaccines.
     */
    suspend fun fetchScheduledVaccines(): List<ScheduledVaccinationGetRequest> {
        val url = "$usersServiceUrl/vaccination/scheduled/user"
        val response = defaultHttpClient.get(url)
        return response.body<List<ScheduledVaccinationGetRequest>>()
    }

    /**
     * Posts a scheduled vaccination.
     *
     * @param scheduledVaccinationPostRequest The scheduled vaccination post request.
     */
    suspend fun postScheduledVaccination(scheduledVaccinationPostRequest: ScheduledVaccinationPostRequest) {
        val url = "$usersServiceUrl/vaccination/scheduled"
        defaultHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(scheduledVaccinationPostRequest)
        }
    }

    /**
     * Gets the list of vaccinations to confirm.
     *
     * @return The list of vaccinations to confirm.
     */
    suspend fun getVaccinationsToConfirm(): List<ScheduledVaccinationGetRequest> {
        val url = "$usersServiceUrl/vaccination/scheduled/confirmation"
        val response = defaultHttpClient.get(url)
        return response.body<List<ScheduledVaccinationGetRequest>>()
    }

    /**
     * Confirms a vaccination.
     *
     * @param scheduledVaccinationId The ID of the scheduled vaccination.
     */
    suspend fun confirmVaccination(scheduledVaccinationId: Long) {
        val url = "$usersServiceUrl/vaccination/scheduled/confirmation/$scheduledVaccinationId"
        defaultHttpClient.patch(url)
    }

    /**
     * Deletes a scheduled vaccination.
     *
     * @param scheduledVaccinationId The ID of the scheduled vaccination.
     */
    suspend fun deleteScheduledVaccination(scheduledVaccinationId: Long) {
        val url = "$usersServiceUrl/vaccination/schedule/$scheduledVaccinationId"
        defaultHttpClient.delete(url)
    }

    /**
     * Fetches a news article.
     *
     * @return The news article.
     */
    suspend fun fetchNewsArticle(): NewsArticle {
        val url =
            "https://newsapi.org/v2/everything?q=vaccines&apiKey=e888e885c27e4d5dbaa85c123c175c0e&pageSize=10"
        val response: HttpResponse = noAuthHttpClient.get(url)
        val newsResponse = response.body<NewsResponse>()
        return newsResponse.articles.random()
    }

    /**
     * Fetches the user details.
     *
     * @param jwtToken The JWT token.
     * @return The user details.
     */
    suspend fun getUserDetails(jwtToken: String): UserDetails {
        val url = "$usersServiceUrl/user/details"
        val response = defaultHttpClient.get(url) {
            headers {
                append("Authorization", "Bearer $jwtToken")
            }
        }
        return response.body<UserDetails>()
    }
    // function to get all users from user/all
    suspend fun getAllUsers(jwtToken: String): List<UserDetails> {
        val url = "$usersServiceUrl/user/all"
        val response = defaultHttpClient.get(url) {
            headers {
                append("Authorization", "Bearer $jwtToken")
            }
        }
        return response.body<List<UserDetails>>()
    }

    suspend fun getScheduledVaccinesForUser(jwtToken: String, userId: Long): List<ScheduledVaccinationGetRequest> {
        val url = "$usersServiceUrl/vaccination/scheduled/user/$userId"
        val response = defaultHttpClient.get(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $jwtToken")
            }
        }
        return response.body<List<ScheduledVaccinationGetRequest>>()
    }
}
