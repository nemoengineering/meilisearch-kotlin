package sh.nemo.meilisearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import sh.nemo.meilisearch.exceptions.ResourceNotFound
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class KeysTest : BaseTest() {
    @Test
    fun `it can list all keys`() =
        runTest {
            // given
            val keyA = client.createKey("Prod key", actions = listOf("documents.add"), indexes = listOf("products"))
            val keyB = client.createKey("Dev key", actions = listOf("documents.delete"), indexes = listOf("products"))

            // when
            val response = client.listKeys()

            // then
            assertEquals(4, response.results.size) // 2 default keys + 2 inserted above
            assertTrue(response.results.containsAll(listOf(keyA, keyB)))
        }

    @Test
    fun `it can create a key`() =
        runTest {
            // given
            val name = "Prod key"
            val actions = listOf("documents.add", "documents.delete")
            val indexes = listOf("products")
            val expiresAt = Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())

            // when
            val response = client.createKey(name = name, actions = actions, indexes = indexes, expiresAt = expiresAt)

            // then
            assertEquals(name, response.name)
            assertEquals(actions, response.actions)
            assertEquals(indexes, response.indexes)
        }

    @Test
    fun `it can get a specific key`() =
        runTest {
            // given
            val keyA = client.createKey("Prod key", actions = listOf("documents.add"), indexes = listOf("products"))

            // when
            val response = client.getKey(keyA.key)

            // then
            assertEquals(keyA.description, response.description)
            assertEquals(keyA.actions, response.actions)
        }

    @Test
    fun `it can update a specific key`() =
        runTest {
            // given
            val key = client.createKey("Prod key", actions = listOf("documents.add"), indexes = listOf("products"))
            val initialKey = client.getKey(key.key)
            assertEquals(key, initialKey)

            // when
            val updatedKey = client.updateKey(key.key, name = "Dev key")

            // then
            assertEquals("Dev key", updatedKey.name)
        }

    @Test
    fun `it can delete a key`() =
        runTest {
            // given
            val key = client.createKey("Prod key", actions = listOf("documents.add"), indexes = listOf("products"))
            val initialKey = client.getKey(key.key)
            assertEquals(key, initialKey)

            // when
            client.deleteKey(key.key)

            // then
            val exception: Exception =
                assertThrows(ResourceNotFound::class.java) {
                    runBlocking {
                        client.getKey(key.key)
                    }
                }

            assertEquals("API key `${key.key}` not found.", exception.message)
        }
}
