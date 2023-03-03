package sh.nemo.meilisearch

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import sh.nemo.meilisearch.models.IndexSettings
import sh.nemo.meilisearch.requests.IndexCreateRequest
import sh.nemo.meilisearch.requests.IndexUpdateRequest
import sh.nemo.meilisearch.responses.ChangeResponse
import sh.nemo.meilisearch.responses.IndexResponse
import sh.nemo.meilisearch.responses.PaginatedResponse

suspend fun Meilisearch.listIndexes(offset: Int = 0, limit: Int = 20): PaginatedResponse<IndexResponse> =
    this.client.get("/indexes") {
        parameter("offset", offset)
        parameter("limit", limit)
    }.body()

suspend fun Meilisearch.createIndex(uid: String, primaryKey: String? = null): ChangeResponse =
    this.client.post("/indexes") {
        setBody(IndexCreateRequest(uid, primaryKey))
    }.body()

suspend fun Meilisearch.getIndex(uid: String): IndexResponse = this.client.get("/indexes/$uid").body()

suspend fun Meilisearch.updateIndexPrimaryKey(uid: String, primaryKey: String? = null): ChangeResponse =
    this.client.patch("/indexes/$uid") {
        setBody(IndexUpdateRequest(primaryKey))
    }.body()

suspend fun Meilisearch.deleteIndex(uid: String): ChangeResponse = this.client.delete("/indexes/$uid").body()

suspend fun Meilisearch.getIndexSettings(uid: String): IndexSettings = this.client.get("/indexes/$uid/settings").body()

suspend fun Meilisearch.updateIndexSettings(uid: String, settings: IndexSettings): ChangeResponse =
    this.client.patch("/indexes/$uid/settings") {
        setBody(settings)
    }.body()
