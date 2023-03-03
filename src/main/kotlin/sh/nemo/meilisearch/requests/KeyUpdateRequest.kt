package sh.nemo.meilisearch.requests

import kotlinx.serialization.Serializable

@Serializable
data class KeyUpdateRequest(
    val name: String? = null,
    val description: String? = null,
)
