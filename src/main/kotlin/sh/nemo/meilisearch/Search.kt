package sh.nemo.meilisearch

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import sh.nemo.meilisearch.requests.SearchRequest
import sh.nemo.meilisearch.responses.SearchResponse

suspend inline fun <reified T> Meilisearch.search(
    indexUid: String,
    query: String = "",
    offset: Int = 0,
    limit: Int = 20,
    hitsPerPage: Int? = null,
    page: Int? = null,
    filter: String? = null,
    facets: List<String>? = null,
    attributesToRetrieve: List<String> = listOf("*"),
    attributesToCrop: List<String>? = null,
    cropLength: Int = 200,
    cropMarker: String = "…",
    attributesToHighlight: List<String>? = null,
    highlightPreTag: String = "<em>",
    highlightPostTag: String = "</em>",
    showMatchesPosition: Boolean = false,
    sort: String? = null,
    matchingStrategy: String = "last",
): SearchResponse<T> =
    this.client
        .post("/indexes/$indexUid/search") {
            setBody(
                SearchRequest(
                    query,
                    offset,
                    limit,
                    hitsPerPage,
                    page,
                    filter,
                    facets,
                    attributesToRetrieve,
                    attributesToCrop,
                    cropLength,
                    cropMarker,
                    attributesToHighlight,
                    highlightPreTag,
                    highlightPostTag,
                    showMatchesPosition,
                    sort,
                    matchingStrategy,
                ),
            )
        }.body()
