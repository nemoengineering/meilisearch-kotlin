package sh.nemo.meilisearch

import io.ktor.client.HttpClient

class Meilisearch(block: MeilisearchClientConfig.() -> Unit) {
    private val config: MeilisearchClientConfig = MeilisearchClientConfig().apply(block)
    var client: HttpClient = httpClient(config)

    class MeilisearchClientConfig {
        lateinit var host: String
        var port: Int = 0
        var apiKey: String? = null
    }
}
