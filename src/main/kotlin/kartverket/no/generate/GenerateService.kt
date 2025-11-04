package kartverket.no.generate

import kartverket.no.airTable.AirTableClientService
import kartverket.no.generate.model.DefaultInitialRiSc
import kartverket.no.generate.model.RiScContent
import kotlinx.serialization.json.Json

object GenerateService {
    /**
     * Generates default RiSc. Currently only a single RiSc type is supported. Therefore, only the first element of the
     * defaultRiScTypes list is used when generating.
     *
     * @param initialRiScContent Content of current initial RiSc before adding content of default RiScs
     * @param defaultRiScId ID of the default RiSc to use for generation
     */
    suspend fun generateDefaultRiSc(
        initialRiScContent: RiScContent,
        defaultRiScId: String,
    ): String =
        generateInitialRiScContent(
            AirTableClientService.fetchDefaultRiScContent(defaultRiScId),
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
