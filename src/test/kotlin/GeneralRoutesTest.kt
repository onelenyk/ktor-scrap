import dev.onelenyk.ktorscrap.presentation.Server
import dev.onelenyk.ktorscrap.presentation.env.EnvironmentManager
import dev.onelenyk.ktorscrap.test.testKoinModule
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import junit.framework.TestCase
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.test.Test

class GeneralRoutesTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            EnvironmentManager.load(arrayOf<String>("FIRESTORE_PROJECT_ID=dondadona"))
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            // Global teardown if necessary
        }
    }
    @Test
    fun testLiveRoute() =
        testApplication {
            application {
                Server().module(this, testKoinModule)
            }
            val response = client.get("/live")
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun testRoutesRoute() =
        testApplication {
            application {
                Server().module(this, testKoinModule)
            }
            val response = client.get("/routes")
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun testRootRoute() =
        testApplication {
            application {
                Server().module(this, testKoinModule)
            }
            val response = client.get("/hello")
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
            TestCase.assertEquals("Hello, Ktor!", response.bodyAsText())
        }

    @Test
    fun testFirestoreIntegrationRoute() =
        testApplication {
            application {
                Server().module(this, testKoinModule)
            }
            val response = client.get("/test/firestore")
            TestCase.assertEquals(HttpStatusCode.OK, response.status)
            TestCase.assertEquals("{\"firestore_test\":\"PASSED\"}", response.bodyAsText())
        }
}
