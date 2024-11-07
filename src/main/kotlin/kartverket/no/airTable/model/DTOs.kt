package kartverket.no.airTable.model

import kartverket.no.exception.exceptions.RetrieveDefaultRiScContentFromAirTableFetchRecordsResponseException
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
