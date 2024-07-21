package sh.nemo.meilisearch.responses

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChangeResponse(
    val taskUid: String,
    val indexUid: String,
    val status: String,
    val type: String,
    val enqueuedAt: Instant,
)
