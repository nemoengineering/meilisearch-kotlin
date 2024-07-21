package sh.nemo.meilisearch.responses

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
)
