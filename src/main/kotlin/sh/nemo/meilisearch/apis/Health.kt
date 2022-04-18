package sh.nemo.meilisearch.apis

import io.ktor.client.call.body
import io.ktor.client.request.get
import sh.nemo.meilisearch.Meilisearch
import sh.nemo.meilisearch.responses.HealthResponse

suspend fun Meilisearch.health(): HealthResponse = this.client.get("/health").body()
