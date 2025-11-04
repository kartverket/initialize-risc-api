package kartverket.no.generate

import io.mockk.coEvery
import io.mockk.mockkObject
import kartverket.no.airTable.AirTableClientService
import kartverket.no.config.AppConfig
import kartverket.no.generate.model.RiScContent
import kartverket.no.generate.model.RiScScenario
import kartverket.no.generate.model.RiScScenarioAction
import kartverket.no.generate.model.RiScScenarioActionInfo
import kartverket.no.generate.model.RiScScenarioActionStatus
import kartverket.no.generate.model.RiScValuation
import kartverket.no.generate.model.Risk
import kartverket.no.generate.model.Scenario
import kartverket.no.generate.model.ThreatActor
import kartverket.no.generate.model.Vulnerability
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
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
        }

        with(AppConfig.airTableConfig) {
            baseUrl = "https://dummy.airtable.com"
            baseId = "dummyBaseId"
            apiToken = "dummyToken"
            recordIdOps = "dummyRecordIdOps"
            recordIdInternalJob = "dummyRecordIdInternalJob"
            recordIdStandard = "dummyRecordIdStandard"
        }
    }

    @Test
    fun `should generate default riSc content with updated title and scope`() =
        runTest {
            mockkObject(AirTableClientService)

            coEvery {
                AirTableClientService.fetchDefaultRiScContent("id")
            } returns
                RiScContent(
                    schemaVersion = "defaultSchema",
                    title = "DefaultTitle",
                    scope = "DefaultScope",
                    valuations = emptyList(),
                    scenarios = emptyList(),
                )

            val inputRiSc =
                RiScContent(
                    schemaVersion = "inputSchema",
                    title = "InputTitle",
                    scope = "InputScope",
                    valuations =
                        listOf(
                            RiScValuation(
                                description = "desc from input",
                                confidentiality = "low",
                                integrity = "low",
                                availability = "low",
                            ),
                        ),
                    scenarios =
                        listOf(
                            RiScScenario(
                                title = "Scenario from input",
                                scenario =
                                    Scenario(
                                        id = "desc",
                                        description = "scenario description",
                                        threatActors = listOf(ThreatActor.SCRIPT_KIDDIE),
                                        vulnerabilities = listOf(Vulnerability.FLAWED_DESIGN),
                                        risk = Risk(consequence = 3, probability = 0.5f),
                                        actions =
                                            listOf(
                                                RiScScenarioAction(
                                                    title = "Some action",
                                                    action =
                                                        RiScScenarioActionInfo(
                                                            id = "a1",
                                                            description = "Action description",
                                                            status = RiScScenarioActionStatus.NOT_OK,
                                                            url = "https://example.com",
                                                        ),
                                                ),
                                            ),
                                        remainingRisk = Risk(consequence = 2, probability = 0.2f),
                                    ),
                            ),
                        ),
                )

            val result = GenerateService.generateDefaultRiSc(inputRiSc, "id")

            val decodedResult = Json.decodeFromString<RiScContent>(result)

            assertEquals("InputTitle", decodedResult.title)
            assertEquals("InputScope", decodedResult.scope)
            assertEquals("defaultSchema", decodedResult.schemaVersion)
            assertTrue(decodedResult.valuations.isEmpty())
            assertTrue(decodedResult.scenarios.isEmpty())
        }
}
