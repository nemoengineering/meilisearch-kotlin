package sh.nemo.meilisearch.responses

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val results: List<T>,
    val offset: Int,
    val limit: Int,
    val total: Int,
)