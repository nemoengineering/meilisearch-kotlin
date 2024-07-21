package sh.nemo.meilisearch.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
    val code: String,
    val type: String,
    val link: String,
)
