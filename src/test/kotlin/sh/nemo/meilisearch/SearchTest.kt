package sh.nemo.meilisearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchTest : BaseTest() {
    @Test
    fun `it can search a document`() =
        runTest {
            // given
            val taskA = client.createIndex("index-1", "id")
            client.waitForTask(taskA.taskUid)

            val docs =
                listOf(
                    ExampleDoc("0", "bob", 42),
                    ExampleDoc("1", "alice", 35),
                )
            val taskB = client.addDocuments("index-1", docs)
            client.waitForTask(taskB.taskUid)

            // when
            val response = client.search<ExampleDoc>("index-1", query = "ali")

            // then
            assertEquals(docs.last(), response.hits.first())
            assertEquals(1, response.estimatedTotalHits)
        }

    @Serializable
    data class ExampleDoc(
        val id: String,
        val name: String,
        val age: Int,
    )
}
