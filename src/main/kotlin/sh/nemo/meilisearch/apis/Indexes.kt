package sh.nemo.meilisearch.apis

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.requests.IndexCreateRequest
import sh.nemo.meilisearch.requests.IndexUpdateRequest
import sh.nemo.meilisearch.responses.ChangeResponse
import sh.nemo.meilisearch.responses.IndexResponse

suspend fun Meilisearch.listIndexes(): List<IndexResponse> = this.client.get("/indexes").body()

suspend fun Meilisearch.createIndex(uid: String, primaryKey: String? = null): ChangeResponse =
    this.client.post("/indexes") {
        setBody(IndexCreateRequest(uid, primaryKey))
    }.body()

suspend fun Meilisearch.getIndex(uid: String): IndexResponse = this.client.get("/indexes/$uid").body()

suspend fun Meilisearch.updateIndexPrimaryKey(uid: String, primaryKey: String? = null): ChangeResponse =
    this.client.put("/indexes/$uid") {
        setBody(IndexUpdateRequest(primaryKey))
    }.body()

suspend fun Meilisearch.deleteIndex(uid: String): ChangeResponse = this.client.delete("/indexes/$uid").body()
