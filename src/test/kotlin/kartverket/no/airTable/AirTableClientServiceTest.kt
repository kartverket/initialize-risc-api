package kartverket.no.descriptor

import io.mockk.coEvery
import io.mockk.spyk
import kartverket.no.airTable.AirTableClientService
import kartverket.no.airTable.model.AirTableFetchRecordsResponse
import kartverket.no.airTable.model.AirTableFields
import kartverket.no.airTable.model.AirTableRecord
import kartverket.no.config.AppConfig
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
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
                    priorityIndex = 2,
                    preferredBackstageComponentType = "operational",
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
                    priorityIndex = 1,
                    preferredBackstageComponentType = null,
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
                    priorityIndex = null,
                    preferredBackstageComponentType = "website",
                ),
        )

    val airTableRecordEmpty =
        AirTableRecord(
            id = "rec-sta",
            fields = AirTableFields(priorityIndex = 1),
        )

    val exampleAirtableFetchResponse =
        AirTableFetchRecordsResponse(
            records = listOf(airTableRecordOps, airTableRecordInternalJob, airTableRecordStandard),
        )

    val sortingTestAirtableFetchResponse =
        AirTableFetchRecordsResponse(
            listOf(
                AirTableRecord("id1", AirTableFields(priorityIndex = 7)),
                AirTableRecord("id2", AirTableFields(priorityIndex = null)),
                AirTableRecord("id3", AirTableFields(priorityIndex = 3)),
                AirTableRecord("id4", AirTableFields(priorityIndex = null)),
                AirTableRecord("id5", AirTableFields(priorityIndex = 9)),
                AirTableRecord("id6", AirTableFields(priorityIndex = 1)),
                AirTableRecord("id7", AirTableFields(priorityIndex = 6)),
                AirTableRecord("id8", AirTableFields(priorityIndex = null)),
                AirTableRecord("id9", AirTableFields(priorityIndex = null)),
                AirTableRecord("id10", AirTableFields(priorityIndex = 5)),
                AirTableRecord("id11", AirTableFields(priorityIndex = 2)),
                AirTableRecord("id12", AirTableFields(priorityIndex = 8)),
                AirTableRecord("id13", AirTableFields(priorityIndex = 4)),
                AirTableRecord("id14", AirTableFields(priorityIndex = null)),
            ),
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
        }
    }

    @Test
    fun `fetchDefaultRiScDescriptors filters out irrelevant riscs`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns exampleAirtableFetchResponse

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(2, descriptors.size)

            assertNotEquals(descriptors[0].id, descriptors[1].id)
            assertTrue(airTableRecordOps.id in descriptors.map { it.id })
            assertTrue(airTableRecordInternalJob.id in descriptors.map { it.id })
            assertFalse(airTableRecordStandard.id in descriptors.map { it.id })
        }

    @Test
    fun `fetchDefaultRiScDescriptors returns descriptors with correct values when riscs have non-null values`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns exampleAirtableFetchResponse

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            val sD = descriptors.find { it.id == airTableRecordOps.id }
            assertNotNull(sD)
            assertEquals(sD.id, airTableRecordOps.id)
            assertEquals(sD.listName, airTableRecordOps.fields.listName)
            assertEquals(sD.listDescription, airTableRecordOps.fields.listDescription)
            assertEquals(sD.defaultTitle, airTableRecordOps.fields.defaultTitle)
            assertEquals(sD.defaultScope, airTableRecordOps.fields.defaultScope)
            assertEquals(sD.numberOfActions, airTableRecordOps.fields.numberOfActions)
            assertEquals(sD.numberOfScenarios, airTableRecordOps.fields.numberOfScenarios)
            assertEquals(sD.preferredBackstageComponentType, airTableRecordOps.fields.preferredBackstageComponentType)
            assertEquals(sD.priorityIndex, airTableRecordOps.fields.priorityIndex)
        }

    @Test
    fun `fetchDefaultRiScDescriptors returns descriptors with correct values when riscs have null values`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns emptyAirtableFetchResponse

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(1, descriptors.size)
            val eD = descriptors[0]
            assertEquals(eD.id, "rec-sta")
            assertEquals(eD.listName, "")
            assertEquals(eD.listDescription, "")
            assertEquals(eD.defaultTitle, "")
            assertEquals(eD.defaultScope, "")
            assertEquals(eD.numberOfActions, null)
            assertEquals(eD.numberOfScenarios, null)
            assertEquals(eD.preferredBackstageComponentType, null)
            assertEquals(eD.priorityIndex, 1)
        }

    @Test
    fun `fetchDefaultRiScDescriptors returns descriptors with correct order`() =
        runTest {
            val airTableClientService = spyk<AirTableClientService>()
            coEvery {
                airTableClientService.fetchDefaultRiScsFromAirTable()
            } returns sortingTestAirtableFetchResponse

            val descriptors = airTableClientService.fetchDefaultRiScDescriptors()

            assertEquals(9, descriptors.size)
            assertEquals("id6", descriptors[0].id)
            assertEquals("id11", descriptors[1].id)
            assertEquals("id3", descriptors[2].id)
            assertEquals("id13", descriptors[3].id)
            assertEquals("id10", descriptors[4].id)
            assertEquals("id7", descriptors[5].id)
            assertEquals("id1", descriptors[6].id)
            assertEquals("id12", descriptors[7].id)
            assertEquals("id5", descriptors[8].id)
        }
}
