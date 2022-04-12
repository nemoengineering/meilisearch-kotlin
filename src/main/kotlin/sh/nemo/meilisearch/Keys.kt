package sh.nemo.meilisearch

import sh.nemo.meilisearch.requests.KeyUpsertRequest
import sh.nemo.meilisearch.responses.KeyResponse
import sh.nemo.meilisearch.responses.KeysResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import kotlinx.datetime.Instant

suspend fun Meilisearch.listKeys() = this.client.get<KeysResponse>("/keys").results

suspend fun Meilisearch.createKey(
    description: String? = null,
    actions: List<String> = emptyList(),
    indexes: List<String> = emptyList(),
    expiresAt: Instant? = null,
) =
    this.client.post<KeyResponse>("/keys") {
        body = KeyUpsertRequest(description, actions, indexes, expiresAt)
    }

suspend fun Meilisearch.getKey(key: String) = this.client.get<KeyResponse>("/keys/$key")

suspend fun Meilisearch.updateKey(
    key: String,
    description: String? = null,
    actions: List<String> = emptyList(),
    indexes: List<String> = emptyList(),
    expiresAt: Instant? = null,
) =
    this.client.patch<KeyResponse>("/keys/$key") {
        body = KeyUpsertRequest(description, actions, indexes, expiresAt)
    }

suspend fun Meilisearch.deleteKey(key: String) = this.client.delete<Unit>("/keys/$key")
