package sh.nemo.meilisearch.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse<T>(
    val hits: List<T>,
    val offset: Int? = null,
    val limit: Int? = null,
    val estimatedTotalHits: Int? = null,
    val totalHits: Int? = null,
    val hitsPerPage: Int? = null,
    val page: Int? = null,
    // val facetsDistribution: Map<String, Map<String, Int>>, FIXME
    val processingTimeMs: Long,
    val query: String
)
