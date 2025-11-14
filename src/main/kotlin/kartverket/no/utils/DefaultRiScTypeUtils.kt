package kartverket.no.utils

import kartverket.no.config.AppConfig
import kartverket.no.descriptor.model.RiScTypeRecordId
import kartverket.no.generate.model.DefaultRiScType

object DefaultRiScTypeUtils {
    private val config = AppConfig.airTableConfig

    private val riScTypeRecordIdMapping: List<RiScTypeRecordId> =
        listOf(
            RiScTypeRecordId(DefaultRiScType.Standard, config.recordIdStandard),
            RiScTypeRecordId(DefaultRiScType.InternalJob, config.recordIdInternalJob),
            RiScTypeRecordId(DefaultRiScType.Ops, config.recordIdOps),
            RiScTypeRecordId(DefaultRiScType.Begrenset, config.recordIdBegrenset),
        )
    private val defaultRiscTypeRecordId = RiScTypeRecordId(DefaultRiScType.Standard, config.recordIdStandard)

    fun getRecordIdFromRiScType(defaultRiScType: DefaultRiScType): String =
        riScTypeRecordIdMapping
            .find {
                it.riScType == defaultRiScType
            }?.recordId ?: defaultRiscTypeRecordId.recordId

    fun getRiScTypeFromRecordId(recordId: String): DefaultRiScType =
        riScTypeRecordIdMapping
            .find {
                it.recordId == recordId
            }?.riScType ?: defaultRiscTypeRecordId.riScType

    fun getAllRecordIds(): Set<String> = riScTypeRecordIdMapping.map { it.recordId }.toSet()
}
