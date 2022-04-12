package sh.nemo.meilisearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import sh.nemo.meilisearch.apis.health
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class HealthTest : BaseTest() {

    @Test
    fun `it can fetch the health of meilisearch service`() = runTest {
        // when
        val response = client.health()

        // then
        assertEquals("available", response.status)
    }
}
