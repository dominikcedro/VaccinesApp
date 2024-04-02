import com.example.vaccineapp.auth.AuthenticationRequest
import com.example.vaccineapp.auth.AuthenticationResponse
import com.example.vaccineapp.auth.RegistrationRequest

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HttpService(private val noAuthHttpClient: HttpClient, private val defaultHttpClient: HttpClient) {
    private val usersServiceUrl = "http://localhost:8080"

    suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        val url = "$usersServiceUrl/auth/login"

        val response: HttpResponse = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            val jsonPayload = Json.encodeToString(authenticationRequest)
            setBody(TextContent(jsonPayload, ContentType.Application.Json))
        }

        return Json.decodeFromString(AuthenticationResponse.serializer(), response.bodyAsText())
    }

    suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResponse {
        val url = "$usersServiceUrl/auth/register"

        val response: HttpResponse = noAuthHttpClient.post(url) {
            contentType(ContentType.Application.Json)
            val jsonPayload = Json.encodeToString(registrationRequest)
            setBody(TextContent(jsonPayload, ContentType.Application.Json))
        }

        return Json.decodeFromString(AuthenticationResponse.serializer(), response.bodyAsText())
    }

}