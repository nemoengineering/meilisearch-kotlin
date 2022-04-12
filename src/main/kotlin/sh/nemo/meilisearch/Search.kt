package sh.nemo.meilisearch

import sh.nemo.meilisearch.requests.SearchRequest
import sh.nemo.meilisearch.responses.SearchResponse
import io.ktor.client.request.post

suspend inline fun <reified T> Meilisearch.search(
    indexUid: String,
    query: String = "",
    offset: Int = 0,
    limit: Int = 20,
    filter: String? = null,
    facetsDistribution: List<String>? = null,
    attributesToRetrieve: List<String> = listOf("*"),
    attributesToCrop: List<String>? = null,
    cropLength: Int = 200,
    attributesToHighlight: List<String>? = null,
    matches: Boolean = false,
    sort: String? = null
) =
    this.client.post<SearchResponse<T>>("/indexes/$indexUid/search") {
        body = SearchRequest(
            query,
            offset,
            limit,
            filter,
            facetsDistribution,
            attributesToRetrieve,
            attributesToCrop,
            cropLength,
            attributesToHighlight,
            matches,
            sort,
        )
    }
