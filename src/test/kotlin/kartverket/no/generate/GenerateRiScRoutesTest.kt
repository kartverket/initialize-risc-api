package kartverket.no.generate

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockkObject
import kartverket.no.airTable.AirTableClientService
import kartverket.no.config.AppConfig
import kartverket.no.generate.model.GenerateRiScRequestBody
import kartverket.no.generate.model.RiScContent
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GenerateRiScRoutesTest {
    @BeforeEach
    fun setup() {
        AppConfig.generateRiScConfig = kartverket.no.config.GenerateRiScConfig
        AppConfig.airTableConfig = kartverket.no.config.AirTableConfig

        with(AppConfig.generateRiScConfig) {
            pathRegex = ".*"
            backendPublicKey = "dummy-backend"
            securityPlatformPublicKey = "dummy-platform"
            securityTeamPublicKey = "dummy-team"
        }

        with(AppConfig.airTableConfig) {
            baseUrl = "https://dummy.airtable.com"
            baseId = "dummyBaseId"
            recordId = "dummyRecordId"
            apiToken = "dummyToken"
        }

        mockkObject(AirTableClientService)

        coEvery {
            AirTableClientService.fetchDefaultRiSc()
        } returns
            RiScContent(
                schemaVersion = "1.0",
                title = "MockTitle",
                scope = "MockScope",
                valuations = emptyList(),
                scenarios = emptyList(),
            )
    }

    @Test
    fun `should return 200 OK and RiSc content on valid input`() =
        testApplication {
            application {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) { json() }
                routing { generateRiScRoutes() }
            }

            val validInitialRiSc =
                """
                {
                  "schemaVersion": "1.0",
                  "title": "TestTitle",
                  "scope": "TestScope",
                  "valuations": [],
                  "scenarios": []
                }
                """.trimIndent()

            val requestBody = GenerateRiScRequestBody(initialRiSc = validInitialRiSc)

            val response =
                client.post("/generate") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(requestBody))
                }

            assertEquals(HttpStatusCode.OK, response.status)
            assertTrue(response.bodyAsText().contains("TestTitle"))
        }

    @Test
    fun `should return 400 Bad Request on invalid initialRiSc`() =
        testApplication {
            application {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) { json() }
                routing { generateRiScRoutes() }
            }

            val invalidRequestBody = """{ "initialRiSc": "not-a-json-object" }"""

            val response =
                client.post("/generate") {
                    contentType(ContentType.Application.Json)
                    setBody(invalidRequestBody)
                }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

    @Test
    fun `should return 400 Bad Request when required fields are missing in initialRiSc`() =
        testApplication {
            application {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) { json() }
                routing { generateRiScRoutes() }
            }

            val invalidInitialRiSc =
                """
                {
                  "schemaVersion": "1.0",
                  "valuations": [],
                  "scenarios": []
                }
                """.trimIndent()

            val requestBody = GenerateRiScRequestBody(initialRiSc = invalidInitialRiSc)

            val response =
                client.post("/generate") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(requestBody))
                }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertTrue(
                response.bodyAsText().contains("not a valid RiSc", ignoreCase = true),
            )
        }
}
