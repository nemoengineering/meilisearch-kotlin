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
    filter: String? = null,
    facetsDistribution: List<String>? = null,
    attributesToRetrieve: List<String> = listOf("*"),
    attributesToCrop: List<String>? = null,
    cropLength: Int = 200,
    attributesToHighlight: List<String>? = null,
    matches: Boolean = false,
    sort: String? = null
): SearchResponse<T> =
    this.client.post("/indexes/$indexUid/search") {
        setBody(
            SearchRequest(
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
        )
    }.body()
