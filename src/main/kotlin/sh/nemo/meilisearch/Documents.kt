package sh.nemo.meilisearch

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import sh.nemo.meilisearch.responses.ChangeResponse

suspend inline fun <reified T> Meilisearch.getDocuments(
    indexUid: String,
    offset: Int = 0,
    limit: Int = 20,
    attributesToRetrieve: String = "*"
): List<T> = this.client.get("/indexes/$indexUid/documents") {
    parameter("offset", offset)
    parameter("limit", limit)
    parameter("attributesToRetrieve", attributesToRetrieve)
}.body()

suspend inline fun <reified T> Meilisearch.addDocuments(
    indexUid: String,
    documents: List<T>,
    primaryKey: String? = null
): ChangeResponse = this.client.post("/indexes/$indexUid/documents") {
    setBody(documents)
    primaryKey?.let { parameter("primaryKey", it) }
}.body()

suspend inline fun <reified T> Meilisearch.updateDocuments(
    indexUid: String,
    documents: List<T>,
    primaryKey: String? = null
): ChangeResponse = this.client.put("/indexes/$indexUid/documents") {
    setBody(documents)
    primaryKey?.let { parameter("primaryKey", it) }
}.body()

suspend inline fun <reified T> Meilisearch.getDocument(indexUid: String, documentId: String): T =
    this.client.get("/indexes/$indexUid/documents/$documentId").body()

suspend fun Meilisearch.deleteAllDocuments(indexUid: String): ChangeResponse =
    this.client.delete("/indexes/$indexUid/documents").body()

suspend fun Meilisearch.deleteDocument(indexUid: String, documentId: String): ChangeResponse =
    this.client.delete("/indexes/$indexUid/documents/$documentId").body()
