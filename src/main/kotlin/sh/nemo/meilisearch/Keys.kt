package sh.nemo.meilisearch

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.Instant
import sh.nemo.meilisearch.requests.KeyCreateRequest
import sh.nemo.meilisearch.requests.KeyUpdateRequest
import sh.nemo.meilisearch.responses.KeyResponse
import sh.nemo.meilisearch.responses.PaginatedResponse

suspend fun Meilisearch.listKeys(offset: Int = 0, limit: Int = 20): PaginatedResponse<KeyResponse> =
    this.client.get("/keys") {
        parameter("offset", offset)
        parameter("limit", limit)
    }.body()

suspend fun Meilisearch.createKey(
    name: String? = null,
    description: String? = null,
    actions: List<String> = emptyList(),
    indexes: List<String> = emptyList(),
    expiresAt: Instant? = null
): KeyResponse =
    this.client.post("/keys") {
        setBody(KeyCreateRequest(name, description, actions, indexes, expiresAt))
    }.body()

suspend fun Meilisearch.getKey(key: String): KeyResponse = this.client.get("/keys/$key").body()

suspend fun Meilisearch.updateKey(key: String, name: String? = null, description: String? = null): KeyResponse =
    this.client.patch("/keys/$key") {
        setBody(KeyUpdateRequest(name, description))
    }.body()

suspend fun Meilisearch.deleteKey(key: String) = this.client.delete("/keys/$key").body<Unit>()
