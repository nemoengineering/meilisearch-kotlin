package sh.nemo.meilisearch

import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
open class BaseTest {

    private val testMasterKey = "testMasterKey"
    protected lateinit var client: Meilisearch

    @Container
    var meilisearch: GenericContainer<*> = GenericContainer(DockerImageName.parse("getmeili/meilisearch:latest"))
        .withExposedPorts(7700).withEnv("MEILI_MASTER_KEY", testMasterKey)

    @BeforeEach
    open fun setUp() {
        client = Meilisearch {
            port = meilisearch.firstMappedPort
            host = meilisearch.host
            apiKey = testMasterKey
        }
    }
}
