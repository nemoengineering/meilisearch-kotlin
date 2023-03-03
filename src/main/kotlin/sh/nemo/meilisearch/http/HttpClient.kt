package sh.nemo.meilisearch.http

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.exceptions.ResourceNotFound
import sh.nemo.meilisearch.responses.ErrorResponse

internal fun httpClient(config: Meilisearch.MeilisearchClientConfig) = HttpClient {
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
