package sh.nemo.meilisearch.models

import kotlinx.serialization.Serializable

@Serializable
data class IndexSettings(
    val displayedAttributes: List<String> = listOf("*"),
    val distinctAttribute: String? = null,
    val filterableAttributes: List<String> = listOf(),
    val rankingRules: List<String> = listOf(
        "words",
        "typo",
        "proximity",
        "attribute",
        "sort",
        "exactness"
    ),
    val searchableAttributes: List<String> = listOf(),
    val stopWords: List<String> = listOf(),
    val synonyms: Map<String, List<String>> = mapOf(),
    val typoTolerance: TypoToleranceSettings = TypoToleranceSettings()
)
