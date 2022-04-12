package sh.nemo.meilisearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import sh.nemo.meilisearch.apis.addDocuments
import sh.nemo.meilisearch.apis.createIndex
import sh.nemo.meilisearch.apis.deleteAllDocuments
import sh.nemo.meilisearch.apis.deleteDocument
import sh.nemo.meilisearch.apis.getDocument
import sh.nemo.meilisearch.apis.getDocuments
import sh.nemo.meilisearch.apis.updateDocuments
import sh.nemo.meilisearch.apis.waitForTask
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class DocumentsTest : BaseTest() {

    @Test
    fun `it can list all documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.uid)

        // when
        val response = client.getDocuments<ExampleDoc>("index-1")

        // then
        assertEquals(2, response.size)
        assertTrue(response.containsAll(docs))
    }

    @Test
    fun `it can add a set of documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        val initialDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(0, initialDocs.size)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )

        // when
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.uid)

        // then
        val insertedDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, insertedDocs.size)
        assertTrue(insertedDocs.containsAll(docs))
    }

    @Test
    fun `it can upsert a set of documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.uid)

        val initialDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, initialDocs.size)
        assertTrue(initialDocs.containsAll(docs))

        val upsertDocs = listOf(
            ExampleDoc("0", "bob", 43),
            ExampleDoc("2", "eve", 60)
        )

        // when
        val taskC = client.updateDocuments("index-1", upsertDocs)
        client.waitForTask(taskC.uid)

        // then
        val upsertedDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(3, upsertedDocs.size)
        assertTrue(
            upsertedDocs.containsAll(
                listOf(
                    ExampleDoc("0", "bob", 43),
                    ExampleDoc("1", "alice", 35),
                    ExampleDoc("2", "eve", 60)
                )
            )
        )
    }

    @Test
    fun `it can get a specific document`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.uid)

        // when
        val response = client.getDocument<ExampleDoc>("index-1", "0")

        // then
        assertEquals(response, docs.first())
    }

    @Test
    fun `it can get delete all documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.uid)

        val initialResponse = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, initialResponse.size)

        // when
        val taskC = client.deleteAllDocuments("index-1")
        client.waitForTask(taskC.uid)

        // then
        val response = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(0, response.size)
    }

    @Test
    fun `it can get delete a specific document`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.uid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.uid)

        val initialResponse = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, initialResponse.size)

        // when
        val taskC = client.deleteDocument("index-1", "0")
        client.waitForTask(taskC.uid)

        // then
        val response = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(1, response.size)
        assertEquals(response.first(), docs.last())
    }

    @Serializable
    data class ExampleDoc(val id: String, val name: String, val age: Int)
}
