package sh.nemo.meilisearch.responses

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class KeyResponse(
    val key: String,
    val description: String? = null,
    val actions: List<String> = emptyList(),
    val indexes: List<String> = emptyList(),
    val expiresAt: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Serializable
data class KeysResponse(val results: List<KeyResponse>)
