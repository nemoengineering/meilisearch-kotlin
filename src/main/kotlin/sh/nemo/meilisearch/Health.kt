package sh.nemo.meilisearch

import io.ktor.client.call.*
import io.ktor.client.request.*
import sh.nemo.meilisearch.responses.HealthResponse

suspend fun Meilisearch.health(): HealthResponse = this.client.get("/health").body()
