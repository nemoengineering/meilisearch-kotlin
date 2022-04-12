package sh.nemo.meilisearch.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse<T>(
    val hits: List<T>,
    val offset: Int,
    val limit: Int,
    val nbHits: Int,
    val exhaustiveNbHits: Boolean,
    // val facetsDistribution: Map<String, Map<String, Int>>, // TODO
    val exhaustiveFacetsCount: Boolean? = null,
    val processingTimeMs: Long,
    val query: String
)
