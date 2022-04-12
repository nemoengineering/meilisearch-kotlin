package sh.nemo.meilisearch.http

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.request.port
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.exceptions.ResourceNotFound
import sh.nemo.meilisearch.responses.ErrorResponse

internal fun httpClient(config: Meilisearch.MeilisearchClientConfig) = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(config.json)
    }

    install(Logging)

    defaultRequest {
        host = config.host
        port = config.port
        config.apiKey?.let { apiKey -> header(HttpHeaders.Authorization, "Bearer $apiKey") }
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }

    HttpResponseValidator {
        handleResponseException { exception ->
            if (exception !is ClientRequestException) return@handleResponseException
            val error = exception.response.receive<ErrorResponse>()
            when (exception.response.status) {
                HttpStatusCode.NotFound -> throw ResourceNotFound(error.message)
            }
        }
    }
}
