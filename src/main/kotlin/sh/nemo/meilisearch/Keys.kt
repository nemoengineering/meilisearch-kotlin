package sh.nemo.meilisearch

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.Instant
import sh.nemo.meilisearch.requests.KeyUpsertRequest
import sh.nemo.meilisearch.responses.KeyResponse
import sh.nemo.meilisearch.responses.KeysResponse

suspend fun Meilisearch.listKeys(): List<KeyResponse> = this.client.get("/keys").body<KeysResponse>().results

suspend fun Meilisearch.createKey(
    description: String? = null,
    actions: List<String> = emptyList(),
    indexes: List<String> = emptyList(),
    expiresAt: Instant? = null
): KeyResponse =
    this.client.post("/keys") {
        setBody(KeyUpsertRequest(description, actions, indexes, expiresAt))
    }.body()

suspend fun Meilisearch.getKey(key: String): KeyResponse = this.client.get("/keys/$key").body()
suspend fun Meilisearch.getKey2(key: String) = this.client.get("/keys/$key")

suspend fun Meilisearch.updateKey(
    key: String,
    description: String? = null,
    actions: List<String> = emptyList(),
    indexes: List<String> = emptyList(),
    expiresAt: Instant? = null
): KeyResponse =
    this.client.patch("/keys/$key") {
        setBody(KeyUpsertRequest(description, actions, indexes, expiresAt))
    }.body()

suspend fun Meilisearch.deleteKey(key: String) = this.client.delete("/keys/$key").body<Unit>()
