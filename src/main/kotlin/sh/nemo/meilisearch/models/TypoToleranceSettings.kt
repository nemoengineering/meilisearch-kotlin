package sh.nemo.meilisearch.models

import kotlinx.serialization.Serializable

@Serializable
data class TypoToleranceSettings(
    val enabled: Boolean = true,
    val minWordSizeForTypos: Map<String, Int> =
        mapOf(
            "oneTypo" to 5,
            "twoTypos" to 9,
        ),
    val disableOnAttributes: List<String> = listOf(),
    val disableOnWords: List<String> = listOf(),
)
