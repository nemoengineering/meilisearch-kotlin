# Meilisearch Kotlin Client

[![](https://jitpack.io/v/nemoengineering/meilisearch-kotlin.svg)](https://jitpack.io/#nemoengineering/meilisearch-kotlin)
[![CD/CI Workflow](https://github.com/nemoengineering/meilisearch-kotlin/actions/workflows/cdci-workflow.yml/badge.svg)](https://github.com/nemoengineering/meilisearch-kotlin/actions/workflows/cdci-workflow.yml)

A Kotlin wrapper around [Meilisearch](https://github.com/meilisearch/meilisearch) REST API. This library uses Ktor
Client and KotlinX Serialization under the hood in order to provide an async typed client for Meilisearch v1.

## Installation

This library is currently available through JitPack.

In order to use the library, add first the JitPack repository to the `repositories` block and then the dependency to
the `dependencies` block:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.nemoengineering:meilisearch-kotlin:0.4.0")
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
