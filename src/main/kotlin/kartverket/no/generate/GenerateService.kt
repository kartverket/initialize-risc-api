package kartverket.no.generate

import kartverket.no.airTable.AirTableClientService
import kartverket.no.config.AppConfig
import kartverket.no.generate.model.CreationRule
import kartverket.no.generate.model.DefaultInitialRiSc
import kartverket.no.generate.model.GcpCryptoKeyObject
import kartverket.no.generate.model.KeyGroup
import kartverket.no.generate.model.PublicAgeKey
import kartverket.no.generate.model.ResourceId
import kartverket.no.generate.model.RiScContent
import kartverket.no.generate.model.SopsConfig
import kartverket.no.generate.model.toResourceId
import kartverket.no.securityMetrics.SecurityMetricsResponse
import kartverket.no.utils.YamlUtils
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object GenerateService {
    private val config = AppConfig.generateRiScConfig

    suspend fun generateDefaultRiSc(
        repositoryName: String,
        initialRiScContent: RiScContent,
    ): String =
        generateInitialRiScContent(
            null,
            AirTableClientService.fetchDefaultRiSc(),
            initialRiScContent,
        ).content

    fun getSopsConfig(
        gcpCryptoKey: GcpCryptoKeyObject,
        publicAgeKeys: List<PublicAgeKey>,
    ) = YamlUtils
        .mapToYamlString(
            SopsConfig(
                creationRules =
                    listOf(
                        CreationRule(
                            pathRegex = config.pathRegex,
                            shamirThreshold = 2,
                            keyGroups =
                                listOfNotNull(
                                    KeyGroup(
                                        age = listOf(config.securityTeamPublicKey),
                                        gcpKms = listOf(ResourceId(gcpCryptoKey.toResourceId())),
                                    ),
                                    KeyGroup(
                                        age = listOf(config.backendPublicKey, config.securityPlatformPublicKey),
                                    ),
                                    if (publicAgeKeys.isNotEmpty()) {
                                        KeyGroup(
                                            age = publicAgeKeys.map { it.value },
                                        )
                                    } else {
                                        null
                                    },
                                ),
                        ),
                    ),
            ),
        ).removePrefix("---\n")

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
