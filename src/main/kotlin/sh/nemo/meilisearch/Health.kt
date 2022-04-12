package sh.nemo.meilisearch

import sh.nemo.meilisearch.responses.HealthResponse
import io.ktor.client.request.get

suspend fun Meilisearch.health() = this.client.get<HealthResponse>("/health")
