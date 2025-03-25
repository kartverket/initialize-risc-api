package kartverket.no.generate
import io.ktor.client.request.post
import io.ktor.server.testing.testApplication
import kotlin.test.Test

class GenerateRiScRoutesTest {
    @Test
    fun testPostGenerateEndpoint1() =
        testApplication {
            routing { generateRiScRoutes() }
            val repositoryName = "backstage-plugin-risk-scorecard-backend"
            val response =
                client.post("/generate/$repositoryName") {
                    // contentType(ContentType.Application.Json) // check if Json is correct
                    // setBody("""{"key: "value"}""")
                }
            // Test at det funker, aka. respons ...
            // assertEquals(HttpStatusCode.OK, response.status)

            // Test respons: RequestValidationException

            // Test respons: IllegalArgumentException

            // Test respons på failet repositoryname
            // assertEquals()
        }
}
