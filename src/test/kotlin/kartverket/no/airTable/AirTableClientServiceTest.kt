package kartverket.no.descriptor

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.spyk
import kartverket.no.airTable.AirTableClientService
import kartverket.no.airTable.model.AirTableFetchRecordsResponse
import kartverket.no.airTable.model.AirTableFields
import kartverket.no.airTable.model.AirTableRecord
import kartverket.no.config.AppConfig
import kartverket.no.generate.model.DefaultRiScType
import kartverket.no.utils.DefaultRiScTypeUtils
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class AirTableClientServiceTest {
    val airTableRecordOps =
        AirTableRecord(
            id = "rec-ops",
            fields =
                AirTableFields(
                    rosJson = "{}",
                    listName = "Ops",
                    listDescription = "An ops job",
                    defaultTitle = "Initial RiSc - Ops",
                    defaultScope = "RiSc generated for <ops>",
                    numberOfScenarios = 3,
                    numberOfActions = 22,
                ),
        )

    val airTableRecordInternalJob =
        AirTableRecord(
            id = "rec-int",
            fields =
                AirTableFields(
                    rosJson = "{}",
                    listName = "Internal Job",
                    listDescription = "An internal job",
                    defaultTitle = "Initial RiSc - Internal Job",
                    defaultScope = "RiSc generated for <internal job>",
                    numberOfScenarios = 5,
                    numberOfActions = 70,
                ),
        )

    val airTableRecordStandard =
        AirTableRecord(
            id = "rec-sta",
            fields =
                AirTableFields(
                    rosJson = "{}",
                    listName = "Standard",
                    listDescription = "A standard job",
                    defaultTitle = "Initial RiSc - Standard",
                    defaultScope = "RiSc generated for <standard>",
                    numberOfScenarios = 2,
                    numberOfActions = 4,
                ),
        )

    val airTableRecordEmpty =
        AirTableRecord(
            id = "rec-sta",
            fields = AirTableFields(),
        )

    val exampleAirtableFetchResponse =
        AirTableFetchRecordsResponse(
            records = listOf(airTableRecordOps, airTableRecordInternalJob, airTableRecordStandard),
        )

    val emptyAirtableFetchResponse =
        AirTableFetchRecordsResponse(
            records = listOf(airTableRecordEmpty),
        )

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
            recordIdOps = "rec-ops"
            recordIdInternalJob = "rec-int"
            recordIdStandard = "rec-sta"
        }
    }

    @Test
    fun `fetchDefaultRiScDescriptors filters out irrelevant riscs`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            mockkObject(DefaultRiScTypeUtils)

            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns exampleAirtableFetchResponse
            every { DefaultRiScTypeUtils.getAllRecordIds() } returns setOf("rec-ops", "rec-int")

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(2, descriptors.size)

            assertNotEquals(descriptors[0].listName, descriptors[1].listName)
            assertTrue(airTableRecordOps.fields.listName in descriptors.map { it.listName })
            assertTrue(airTableRecordInternalJob.fields.listName in descriptors.map { it.listName })
            assertFalse(airTableRecordStandard.fields.listName in descriptors.map { it.listName })
        }

    @Test
    fun `fetchDefaultRiScDescriptors does not fetch riscs when ids are wrong`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            mockkObject(DefaultRiScTypeUtils)

            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns exampleAirtableFetchResponse
            every { DefaultRiScTypeUtils.getAllRecordIds() } returns setOf("rec-does-not-exist", "rec-int")

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(1, descriptors.size)
        }

    @Test
    fun `fetchDefaultRiScDescriptors sets correct risc type`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            mockkObject(DefaultRiScTypeUtils)

            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns exampleAirtableFetchResponse
            every { DefaultRiScTypeUtils.getAllRecordIds() } returns setOf("rec-ops", "rec-int", "rec-sta")

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(3, descriptors.size)

            var containsStandard = false
            var containsInternalJob = false
            var containsOps = false

            for (descriptor in descriptors) {
                when (descriptor.riScType) {
                    DefaultRiScType.Standard -> containsStandard = true
                    DefaultRiScType.InternalJob -> containsInternalJob = true
                    DefaultRiScType.Ops -> containsOps = true
                }
            }
            assertTrue(containsStandard)
            assertTrue(containsInternalJob)
            assertTrue(containsOps)
        }

    @Test
    fun `fetchDefaultRiScDescriptors returns descriptors with correct values when riscs have non-null values`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            mockkObject(DefaultRiScTypeUtils)

            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns exampleAirtableFetchResponse
            every { DefaultRiScTypeUtils.getAllRecordIds() } returns setOf("rec-sta")

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(1, descriptors.size)
            val sD = descriptors[0]
            assertEquals(sD.riScType, DefaultRiScType.Standard)
            assertEquals(sD.listName, airTableRecordStandard.fields.listName)
            assertEquals(sD.listDescription, airTableRecordStandard.fields.listDescription)
            assertEquals(sD.defaultTitle, airTableRecordStandard.fields.defaultTitle)
            assertEquals(sD.defaultScope, airTableRecordStandard.fields.defaultScope)
            assertEquals(sD.numberOfActions, airTableRecordStandard.fields.numberOfActions)
            assertEquals(sD.numberOfScenarios, airTableRecordStandard.fields.numberOfScenarios)
        }

    @Test
    fun `fetchDefaultRiScDescriptors returns descriptors with correct values when riscs have null values`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            mockkObject(DefaultRiScTypeUtils)

            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns emptyAirtableFetchResponse
            every { DefaultRiScTypeUtils.getAllRecordIds() } returns setOf("rec-sta")

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(1, descriptors.size)
            val eD = descriptors[0]
            assertEquals(eD.riScType, DefaultRiScType.Standard)
            assertEquals(eD.listName, "Unknown name")
            assertEquals(eD.listDescription, "Unknown description")
            assertEquals(eD.defaultTitle, "")
            assertEquals(eD.defaultScope, "")
            assertEquals(eD.numberOfActions, null)
            assertEquals(eD.numberOfScenarios, null)
        }
}
