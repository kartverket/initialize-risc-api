package kartverket.no.utils

import kartverket.no.config.AppConfig
import kartverket.no.generate.model.DefaultRiScType

object DefaultRiScTypeUtils {
    private val config = AppConfig.airTableConfig

    fun getRecordIdFromRiScType(defaultRiScType: DefaultRiScType): String =
        when (defaultRiScType) {
            DefaultRiScType.Ops -> config.recordIdOps
            DefaultRiScType.InternalJob -> config.recordIdInternalJob
            DefaultRiScType.Standard -> config.recordIdStandard
        }

    fun getRiScTypeFromRecordId(recordId: String): DefaultRiScType =
        when (recordId) {
            config.recordIdOps -> DefaultRiScType.Ops
            config.recordIdInternalJob -> DefaultRiScType.InternalJob
            config.recordIdStandard -> DefaultRiScType.Standard
            else -> DefaultRiScType.Standard
        }

    fun getAllRecordIds(): Set<String> = setOf(config.recordIdOps, config.recordIdInternalJob, config.recordIdStandard)
}
