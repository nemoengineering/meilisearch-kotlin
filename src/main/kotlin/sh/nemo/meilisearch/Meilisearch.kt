package sh.nemo.meilisearch

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import sh.nemo.meilisearch.http.httpClient

class Meilisearch(block: MeilisearchClientConfig.() -> Unit) {
    private val config: MeilisearchClientConfig = MeilisearchClientConfig().apply(block)
    var client: HttpClient = httpClient(config)

    class MeilisearchClientConfig {
        private val defaultJson by lazy {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }
        }

        lateinit var host: String
        var port: Int = 0
        var apiKey: String? = null
        var json: Json = defaultJson
    }
}
