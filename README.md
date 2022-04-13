# Meilisearch Kotlin Client

A Kotlin wrapper around [Meilisearch](https://github.com/meilisearch/meilisearch) REST API. This library uses Ktor
Client and KotlinX Serialization under the hood in order to provide an async typed client for Meilisearch.

## Installation

This library is currently available through JitPack.

In order to use the library, add first the JitPack repository to the `repositories` block and then the dependency to
the `dependencies` block:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.nemoengineering:meilisearch-kotlin:0.1.1")
}
```

## Usage

In order to interact with the Meilisearch you need to first instantiate a client which can then be used to perform the
operations supported by the API:

```kotlin
val client = Meilisearch {
    port = 7700
    host = "localhost"
    apiKey = "my-api-key" // optional
}

val response: List<IndexResponse> = client.listIndexes()
```
