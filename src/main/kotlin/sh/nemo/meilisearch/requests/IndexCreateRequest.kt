package sh.nemo.meilisearch.requests

import kotlinx.serialization.Serializable

@Serializable
data class IndexCreateRequest(
    val uid: String,
    val primaryKey: String?
)
