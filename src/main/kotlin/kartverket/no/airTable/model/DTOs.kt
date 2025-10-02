package kartverket.no.airTable.model

import kartverket.no.descriptor.model.RiScDescriptor
import kartverket.no.exception.exceptions.RetrieveDefaultRiScContentFromAirTableFetchRecordsResponseException
import kartverket.no.generate.model.DefaultRiScType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.Logger

@Serializable
data class AirTableFetchRecordsResponse(
    val records: List<AirTableRecord>,
) {
    fun toRiScContentString(
        logger: Logger,
        recordId: String,
    ) = this.records
        .firstOrNull { it.id == recordId }
        ?.fields
        ?.rosJson
        ?: throw RetrieveDefaultRiScContentFromAirTableFetchRecordsResponseException(
            logger,
            "Unable to retrieve default RiSc JSON from AirTable response",
        )
}

@Serializable
data class AirTableRecord(
    val id: String,
    val fields: AirTableFields,
)

@Serializable
data class AirTableFields(
    val rosJson: String,
)

@Serializable
data class AirTableFetchRecordsResponseOnlyMetadata(
    val records: List<AirTableRecordOnlyMetadata>,
)

@Serializable
data class AirTableRecordOnlyMetadata(
    val id: String,
    val fields: AirTableFieldsOnlyMetadata,
)

@Serializable
data class AirTableFieldsOnlyMetadata(
    @SerialName("List-name") val listName: String? = null,
    @SerialName("List-description") val listDescription: String? = null,
    @SerialName("Title") val defaultTitle: String? = null,
    @SerialName("Scope") val defaultScope: String? = null,
    @SerialName("Antall scenarier") val numberOfScenarios: Int? = null,
    @SerialName("Antall tiltak") val numberOfActions: Int? = null,
) {
    fun toRiScDescriptor(defaultRiScType: DefaultRiScType): RiScDescriptor =
        RiScDescriptor(
            riScType = defaultRiScType,
            listName = listName ?: "Unknown name",
            listDescription = listDescription ?: "Unknown description",
            defaultTitle = defaultTitle ?: "",
            defaultScope = defaultScope ?: "",
        )
}
