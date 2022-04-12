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
import sh.nemo.meilisearch.apis.createKey
import sh.nemo.meilisearch.apis.deleteKey
import sh.nemo.meilisearch.apis.getKey
import sh.nemo.meilisearch.apis.listKeys
import sh.nemo.meilisearch.apis.updateKey
import sh.nemo.meilisearch.exceptions.ResourceNotFound
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class KeysTest : BaseTest() {

    @Test
    fun `it can list all keys`() = runTest {
        // given
        val keyA = client.createKey("Prod key", listOf("documents.add"), listOf("products"))
        val keyB = client.createKey("Dev key", listOf("documents.delete"), listOf("products"))

        // when
        val response = client.listKeys()

        // then
        assertEquals(4, response.size) // 2 default keys + 2 inserted above
        assertTrue(response.containsAll(listOf(keyA, keyB)))
    }

    @Test
    fun `it can create a key`() = runTest {
        // given
        val description = "Prod key"
        val actions = listOf("documents.add", "documents.delete")
        val indexes = listOf("products")
        val expiresAt = Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())

        // when
        val response = client.createKey(description, actions, indexes, expiresAt)

        // then
        assertEquals(description, response.description)
        assertEquals(actions, response.actions)
        assertEquals(indexes, response.indexes)
    }

    @Test
    fun `it can get a specific key`() = runTest {
        // given
        val keyA = client.createKey("Prod key", listOf("documents.add"), listOf("products"))

        // when
        val response = client.getKey(keyA.key)

        // then
        assertEquals(keyA.description, response.description)
        assertEquals(keyA.actions, response.actions)
    }

    @Test
    fun `it can update a specific key`() = runTest {
        // given
        val key = client.createKey("Prod key", listOf("documents.add"), listOf("products"))
        val initialKey = client.getKey(key.key)
        assertEquals(key, initialKey)

        // when
        val updatedKey = client.updateKey(key.key, actions = listOf("documents.delete"))

        // then
        assertEquals(listOf("documents.delete"), updatedKey.actions)
    }

    @Test
    fun `it can delete a key`() = runTest {
        // given
        val key = client.createKey("Prod key", listOf("documents.add"), listOf("products"))
        val initialKey = client.getKey(key.key)
        assertEquals(key, initialKey)

        // when
        client.deleteKey(key.key)

        // then
        val exception: Exception = assertThrows(ResourceNotFound::class.java) {
            runBlocking {
                client.getKey(key.key)
            }
        }

        assertEquals("API key `${key.key}` not found.", exception.message)
    }
}
