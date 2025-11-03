package kartverket.no.airTable.model

import kartverket.no.airTable.AirTableClientService
import kartverket.no.descriptor.model.RiScDescriptor
import kartverket.no.exception.exceptions.RetrieveDefaultRiScContentFromAirTableFetchRecordsResponseException
import kartverket.no.generate.model.RiScContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@Serializable
data class AirTableFetchRecordsResponse(
    val records: List<AirTableRecord>,
)

@Serializable
data class AirTableRecord(
    val id: String,
    val fields: AirTableFields,
) {
    fun toRiScDescriptor(): RiScDescriptor =
        RiScDescriptor(
            id = id,
            listName = fields.listName ?: "",
            listDescription = fields.listDescription ?: "",
            defaultTitle = fields.defaultTitle ?: "",
            defaultScope = fields.defaultScope ?: "",
            numberOfScenarios = fields.numberOfScenarios,
            numberOfActions = fields.numberOfActions,
            preferredEntityType = fields.preferredEntityType,
        )
}

@Serializable
data class AirTableFields(
    val rosJson: String? = null,
    @SerialName("List-name") val listName: String? = null,
    @SerialName("List-description") val listDescription: String? = null,
    @SerialName("Title") val defaultTitle: String? = null,
    @SerialName("Scope") val defaultScope: String? = null,
    @SerialName("Antall scenarier") val numberOfScenarios: Int? = null,
    @SerialName("Antall tiltak") val numberOfActions: Int? = null,
    @SerialName("orderIndex") val orderIndex: Int? = null,
    @SerialName("preferredEntityType") val preferredEntityType: String? = null,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(AirTableClientService::class.java)
        private val json = Json { ignoreUnknownKeys = true }
    }

    fun toRiScContent(): RiScContent {
        if (rosJson == null) {
            throw RetrieveDefaultRiScContentFromAirTableFetchRecordsResponseException(
                logger,
                "Unable to retrieve default RiSc JSON from AirTable response",
            )
        }
        return json.decodeFromString<RiScContent>(rosJson)
    }
}
