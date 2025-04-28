package kartverket.no.generate

import kartverket.no.airTable.AirTableClientService
import kartverket.no.generate.model.DefaultInitialRiSc
import kartverket.no.generate.model.RiScContent
import kotlinx.serialization.json.Json

object GenerateService {
    suspend fun generateDefaultRiSc(initialRiScContent: RiScContent): String =
        generateInitialRiScContent(
            AirTableClientService.fetchDefaultRiSc(),
            initialRiScContent,
        ).content

    private fun generateInitialRiScContent(
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
