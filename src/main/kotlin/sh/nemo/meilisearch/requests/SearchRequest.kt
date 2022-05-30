package sh.nemo.meilisearch.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerialName("q") val query: String,
    val offset: Int,
    val limit: Int,
    val filter: String?,
    val facetsDistribution: List<String>?,
    val attributesToRetrieve: List<String>,
    val attributesToCrop: List<String>?,
    val cropLength: Int,
    val cropMarker: String,
    val attributesToHighlight: List<String>?,
    val highlightPreTag: String,
    val highlightPostTag: String,
    val matches: Boolean,
    val sort: String?
)
