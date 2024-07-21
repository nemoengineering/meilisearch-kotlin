package sh.nemo.meilisearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import sh.nemo.meilisearch.exceptions.ResourceNotFound
import sh.nemo.meilisearch.models.IndexSettings
import sh.nemo.meilisearch.models.TypoToleranceSettings
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class IndexesTest : BaseTest() {
    @Test
    fun `it can list all indexes`() =
        runTest {
            // given
            val taskA = client.createIndex("index-1", "id")
            val taskB = client.createIndex("index-2", "name")

            client.waitForTask(taskA.taskUid)
            client.waitForTask(taskB.taskUid)

            // when
            val response = client.listIndexes()

            // then
            assertEquals(2, response.results.size)
            assertTrue(response.results.any { it.uid == "index-1" && it.primaryKey == "id" })
            assertTrue(response.results.any { it.uid == "index-2" && it.primaryKey == "name" })
        }

    @Test
    fun `it can create an index`() =
        runTest {
            // given
            val uid = "index-alpha"
            val primaryKey = "id"

            // when
            val response = client.createIndex(uid, primaryKey)

            // then
            assertEquals(uid, response.indexUid)
            assertEquals("enqueued", response.status)
            assertEquals("indexCreation", response.type)
        }

    @Test
    fun `it can get a specific index`() =
        runTest {
            // given
            val taskA = client.createIndex("index-1", "id")
            client.waitForTask(taskA.taskUid)

            // when
            val response = client.getIndex("index-1")

            // then
            assertEquals("index-1", response.uid)
            assertEquals("id", response.primaryKey)
        }

    @Test
    fun `it can update the primary key for a specific index`() =
        runTest {
            // given
            val taskA = client.createIndex("index-1", "id")
            client.waitForTask(taskA.taskUid)
            val initialIndex = client.getIndex("index-1")
            assertEquals("id", initialIndex.primaryKey)

            // when
            val taskB = client.updateIndexPrimaryKey("index-1", "address")
            client.waitForTask(taskB.taskUid)

            // then
            val updatedIndex = client.getIndex("index-1")
            assertEquals("address", updatedIndex.primaryKey)
        }

    @Test
    fun `it can delete an index`() =
        runTest {
            // given
            val taskA = client.createIndex("index-1", "id")
            client.waitForTask(taskA.taskUid)

            // when
            val taskB = client.deleteIndex("index-1")
            client.waitForTask(taskB.taskUid)

            // then
            val exception: Exception =
                assertThrows(ResourceNotFound::class.java) {
                    runBlocking {
                        client.getIndex("index-1")
                    }
                }

            assertEquals("Index `index-1` not found.", exception.message)
        }

    @Test
    fun `it can update index settings`() =
        runTest {
            // given
            val taskA = client.createIndex("index-1", "id")
            client.waitForTask(taskA.taskUid)

            // when
            val settings =
                IndexSettings(
                    displayedAttributes = listOf("attr1"),
                    distinctAttribute = "attr1",
                    filterableAttributes = listOf("attr1"),
                    rankingRules = listOf("typo", "sort"),
                    searchableAttributes = listOf("attr1"),
                    stopWords = listOf("stop-word-1"),
                    synonyms =
                        mapOf(
                            "syn1" to listOf("syn2", "syn3"),
                        ),
                    typoTolerance =
                        TypoToleranceSettings(
                            minWordSizeForTypos =
                                mapOf(
                                    "oneTypo" to 2,
                                    "twoTypos" to 2,
                                ),
                            disableOnAttributes = listOf("attr1"),
                            disableOnWords = listOf("word"),
                        ),
                )
            val taskB = client.updateIndexSettings("index-1", settings)
            client.waitForTask(taskB.taskUid)

            // then
            val resSettings = client.getIndexSettings("index-1")

            assertEquals(settings, resSettings)
        }
}
