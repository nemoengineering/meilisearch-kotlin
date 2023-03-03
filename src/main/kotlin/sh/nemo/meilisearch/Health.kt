package sh.nemo.meilisearch

import io.ktor.client.call.body
import io.ktor.client.request.get
import sh.nemo.meilisearch.responses.HealthResponse

suspend fun Meilisearch.health(): HealthResponse = this.client.get("/health").body()
