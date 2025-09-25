package kartverket.no.generate

import kartverket.no.airTable.AirTableClientService
import kartverket.no.generate.model.DefaultInitialRiSc
import kartverket.no.generate.model.DefaultRiScType
import kartverket.no.generate.model.RiScContent
import kotlinx.serialization.json.Json

object GenerateService {
    /**
     * Generates default RiSc. Currently only a single RiSc type is supported. Therefore, only the first element of the
     * defaultRiScTypes list is used when generating.
     *
     * @param initialRiScContent Content of current initial RiSc before adding content of default RiScs
     * @param defaultRiScTypes The RiSc types from which the default RiSc will be generated.
     */
    suspend fun generateDefaultRiSc(
        initialRiScContent: RiScContent,
        defaultRiScTypes: List<DefaultRiScType>,
    ): String {
        // For now only a single default risc is fetched (The first risc in the defaultRiScs list)
        val defaultRiScToFetch =
            if (defaultRiScTypes.isEmpty()) {
                DefaultRiScType.Standard
            } else {
                defaultRiScTypes[0]
            }
        return generateInitialRiScContent(
            AirTableClientService.fetchDefaultRiSc(defaultRiScToFetch),
            initialRiScContent,
        ).content
    }

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
