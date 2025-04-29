package kartverket.no.generate

import io.mockk.coEvery
import io.mockk.mockkObject
import kartverket.no.airTable.AirTableClientService
import kartverket.no.config.AppConfig
import kartverket.no.generate.model.RiScContent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GenerateServiceTest {
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
    }

    @Test
    fun `should generate default riSc content with updated title and scope`() =
        runTest {
            mockkObject(AirTableClientService)

            coEvery {
                AirTableClientService.fetchDefaultRiSc()
            } returns
                RiScContent(
                    schemaVersion = "1.0",
                    title = "DefaultTitle",
                    scope = "DefaultScope",
                    valuations = emptyList(),
                    scenarios = emptyList(),
                )

            val inputRiSc =
                RiScContent(
                    schemaVersion = "1.0",
                    title = "InputTitle",
                    scope = "InputScope",
                    valuations = emptyList(),
                    scenarios = emptyList(),
                )

            val result = GenerateService.generateDefaultRiSc(inputRiSc)

            assertTrue(result.contains("InputTitle"))
            assertTrue(result.contains("InputScope"))
        }
}
