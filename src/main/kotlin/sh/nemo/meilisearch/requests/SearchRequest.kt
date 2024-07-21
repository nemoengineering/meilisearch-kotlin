package sh.nemo.meilisearch.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerialName("q") val query: String,
    val offset: Int,
    val limit: Int,
    val hitsPerPage: Int?,
    val page: Int?,
    val filter: String?,
    val facets: List<String>?,
    val attributesToRetrieve: List<String>,
    val attributesToCrop: List<String>?,
    val cropLength: Int,
    val cropMarker: String,
    val attributesToHighlight: List<String>?,
    val highlightPreTag: String,
    val highlightPostTag: String,
    val showMatchesPosition: Boolean,
    val sort: String?,
    val matchingStrategy: String,
    val showRankingScore: Boolean,
    val showRankingScoreDetails: Boolean,
    val rankingScoreThreshold: Double?,
    val attributesToSearchOn: List<String>?,
)
