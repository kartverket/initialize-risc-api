package kartverket.no.generate

import kartverket.no.airTable.AirTableClientService
import kartverket.no.config.AppConfig
import kartverket.no.generate.model.DefaultInitialRiSc
import kartverket.no.generate.model.RiScContent
import kartverket.no.securityMetrics.SecurityMetricsResponse
import kotlinx.serialization.json.Json

object GenerateService {
    private val config = AppConfig.generateRiScConfig

    suspend fun generateDefaultRiSc(initialRiScContent: RiScContent): String =
        generateInitialRiScContent(
            null,
            AirTableClientService.fetchDefaultRiSc(),
            initialRiScContent,
        ).content

    private fun generateInitialRiScContent(
        securityMetrics: SecurityMetricsResponse?,
        defaultRiSc: RiScContent,
        initialRiScContent: RiScContent,
    ): DefaultInitialRiSc =
        DefaultInitialRiSc(
            Json.encodeToString(
                defaultRiSc.copy(
                    title = initialRiScContent.title,
                    scope = initialRiScContent.scope,
                ),
            ),
        )
}
