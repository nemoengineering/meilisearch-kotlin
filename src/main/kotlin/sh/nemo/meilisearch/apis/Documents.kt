package sh.nemo.meilisearch.apis

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.responses.ChangeResponse

suspend inline fun <reified T> Meilisearch.getDocuments(
    indexUid: String,
    offset: Int = 0,
    limit: Int = 20,
    attributesToRetrieve: String = "*"
) = this.client.get<List<T>>("/indexes/$indexUid/documents") {
    parameter("offset", offset)
    parameter("limit", limit)
    parameter("attributesToRetrieve", attributesToRetrieve)
}

suspend inline fun <reified T> Meilisearch.addDocuments(
    indexUid: String,
    documents: List<T>,
    primaryKey: String? = null
) = this.client.post<ChangeResponse>("/indexes/$indexUid/documents") {
    body = documents
    primaryKey?.let { parameter("primaryKey", it) }
}

suspend inline fun <reified T> Meilisearch.updateDocuments(
    indexUid: String,
    documents: List<T>,
    primaryKey: String? = null
) = this.client.put<ChangeResponse>("/indexes/$indexUid/documents") {
    body = documents
    primaryKey?.let { parameter("primaryKey", it) }
}

suspend inline fun <reified T> Meilisearch.getDocument(indexUid: String, documentId: String) =
    this.client.get<T>("/indexes/$indexUid/documents/$documentId")

suspend fun Meilisearch.deleteAllDocuments(indexUid: String) =
    this.client.delete<ChangeResponse>("/indexes/$indexUid/documents")

suspend fun Meilisearch.deleteDocument(indexUid: String, documentId: String) =
    this.client.delete<ChangeResponse>("/indexes/$indexUid/documents/$documentId")
