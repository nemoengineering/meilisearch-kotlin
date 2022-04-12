package sh.nemo.meilisearch.responses

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val uid: String,
    val status: String,
    val indexUid: String,
    val type: String,
    val duration: String? = null,
    val enqueuedAt: Instant? = null,
    val startedAt: Instant? = null,
    val finishedAt: Instant? = null,
    val error: ErrorResponse? = null,
    // val details: String? = null,
)
