package sh.nemo.meilisearch

import sh.nemo.meilisearch.exceptions.ResourceNotFound
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class IndexesTest : BaseTest() {

    @Test
    fun `it can list all indexes`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        val taskB = client.createIndex("index-2", "name")

        client.waitForTask(taskA.uid)
        client.waitForTask(taskB.uid)

        // when
        val response = client.listIndexes()

        // then
        assertEquals(2, response.size)
        assertTrue(response.any { it.uid == "index-1" && it.primaryKey == "id" })
        assertTrue(response.any { it.uid == "index-2" && it.primaryKey == "name" })
    }

    @Test
    fun `it can create an index`() = runTest {
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
    fun `it can get a specific index`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        // when
        val response = client.getIndex("index-1")

        // then
        assertEquals("index-1", response.uid)
        assertEquals("id", response.primaryKey)
    }

    @Test
    fun `it can update the primary key for a specific index`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)
        val initialIndex = client.getIndex("index-1")
        assertEquals("id", initialIndex.primaryKey)

        // when
        val taskB = client.updateIndexPrimaryKey("index-1", "address")
        client.waitForTask(taskB.uid)

        // then
        val updatedIndex = client.getIndex("index-1")
        assertEquals("address", updatedIndex.primaryKey)
    }

    @Test
    fun `it can delete an index`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        // when
        val taskB = client.deleteIndex("index-1")
        client.waitForTask(taskB.uid)

        // then
        val exception: Exception = assertThrows(ResourceNotFound::class.java) {
            runBlocking {
                client.getIndex("index-1")
            }
        }

        assertEquals("Index `index-1` not found.", exception.message)
    }
}
