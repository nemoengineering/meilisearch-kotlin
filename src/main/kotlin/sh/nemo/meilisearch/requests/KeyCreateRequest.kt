package sh.nemo.meilisearch.requests

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class KeyCreateRequest(
    val name: String? = null,
    val description: String? = null,
    val actions: List<String> = emptyList(),
    val indexes: List<String> = emptyList(),
    val expiresAt: Instant? = null
)
