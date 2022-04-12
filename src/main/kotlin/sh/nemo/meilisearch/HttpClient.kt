package sh.nemo.meilisearch

import sh.nemo.meilisearch.exceptions.ResourceNotFound
import sh.nemo.meilisearch.responses.ErrorResponse
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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.util.UUID

internal fun httpClient(config: Meilisearch.MeilisearchClientConfig) = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
                serializersModule = SerializersModule {
                    contextual(UUIDSerializer())
                }
            }
        )
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

class UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) =
        encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder) =
        UUID.fromString(decoder.decodeString())
}
