package sh.nemo.meilisearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class DocumentsTest : BaseTest() {

    @Test
    fun `it can list all documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.taskUid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.taskUid)

        // when
        val response = client.getDocuments<ExampleDoc>("index-1")

        // then
        assertEquals(2, response.results.size)
        assertTrue(response.results.containsAll(docs))
    }

    @Test
    fun `it can add a set of documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.taskUid)

        val initialDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(0, initialDocs.results.size)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )

        // when
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.taskUid)

        // then
        val insertedDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, insertedDocs.results.size)
        assertTrue(insertedDocs.results.containsAll(docs))
    }

    @Test
    fun `it can upsert a set of documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.taskUid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.taskUid)

        val initialDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, initialDocs.results.size)
        assertTrue(initialDocs.results.containsAll(docs))

        val upsertDocs = listOf(
            ExampleDoc("0", "bob", 43),
            ExampleDoc("2", "eve", 60)
        )

        // when
        val taskC = client.updateDocuments("index-1", upsertDocs)
        client.waitForTask(taskC.taskUid)

        // then
        val upsertedDocs = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(3, upsertedDocs.results.size)
        assertTrue(
            upsertedDocs.results.containsAll(
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
        client.waitForTask(taskA.taskUid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.taskUid)

        // when
        val response = client.getDocument<ExampleDoc>("index-1", "0")

        // then
        assertEquals(response, docs.first())
    }

    @Test
    fun `it can get delete all documents`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.taskUid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.taskUid)

        val initialResponse = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, initialResponse.results.size)

        // when
        val taskC = client.deleteAllDocuments("index-1")
        client.waitForTask(taskC.taskUid)

        // then
        val response = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(0, response.results.size)
    }

    @Test
    fun `it can get delete a specific document`() = runTest {
        // given
        val taskA = client.createIndex("index-1", "id")
        client.waitForTask(taskA.taskUid)

        val docs = listOf(
            ExampleDoc("0", "bob", 42),
            ExampleDoc("1", "alice", 35)
        )
        val taskB = client.addDocuments("index-1", docs)
        client.waitForTask(taskB.taskUid)

        val initialResponse = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(2, initialResponse.results.size)

        // when
        val taskC = client.deleteDocument("index-1", "0")
        client.waitForTask(taskC.taskUid)

        // then
        val response = client.getDocuments<ExampleDoc>("index-1")
        assertEquals(1, response.results.size)
        assertEquals(response.results.first(), docs.last())
    }

    @Serializable
    data class ExampleDoc(val id: String, val name: String, val age: Int)
}
