package sh.nemo.meilisearch.responses

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class IndexResponse(
    val uid: String,
    val primaryKey: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
