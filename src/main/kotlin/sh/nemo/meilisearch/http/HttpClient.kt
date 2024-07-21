package sh.nemo.meilisearch.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.exceptions.ResourceNotFound
import sh.nemo.meilisearch.responses.ErrorResponse

internal fun httpClient(config: Meilisearch.MeilisearchClientConfig) =
    HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(config.json)
        }

        install(Logging)

        defaultRequest {
            host = config.host
            port = config.port
            config.apiKey?.let { apiKey -> header(HttpHeaders.Authorization, "Bearer $apiKey") }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                if (exception !is ClientRequestException) return@handleResponseExceptionWithRequest
                val error = exception.response.body<ErrorResponse>()
                when (exception.response.status) {
                    HttpStatusCode.NotFound -> throw ResourceNotFound(error.message)
                }
            }
        }
    }
