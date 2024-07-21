package sh.nemo.meilisearch.requests

import kotlinx.serialization.Serializable

@Serializable
data class IndexUpdateRequest(
    val primaryKey: String?,
)
