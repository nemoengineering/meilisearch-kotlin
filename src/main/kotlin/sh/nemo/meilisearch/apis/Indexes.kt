package sh.nemo.meilisearch.apis

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.requests.IndexCreateRequest
import sh.nemo.meilisearch.requests.IndexUpdateRequest
import sh.nemo.meilisearch.responses.ChangeResponse
import sh.nemo.meilisearch.responses.IndexResponse

suspend fun Meilisearch.listIndexes() = this.client.get<List<IndexResponse>>("/indexes")

suspend fun Meilisearch.createIndex(uid: String, primaryKey: String? = null) =
    this.client.post<ChangeResponse>("/indexes") {
        body = IndexCreateRequest(uid, primaryKey)
    }

suspend fun Meilisearch.getIndex(uid: String) = this.client.get<IndexResponse>("/indexes/$uid")

suspend fun Meilisearch.updateIndexPrimaryKey(uid: String, primaryKey: String? = null) =
    this.client.put<ChangeResponse>("/indexes/$uid") {
        body = IndexUpdateRequest(primaryKey)
    }

suspend fun Meilisearch.deleteIndex(uid: String) = this.client.delete<ChangeResponse>("/indexes/$uid")
