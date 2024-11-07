package kartverket.no.generateRiSc

import kartverket.no.airTable.AirTableClientService
import kartverket.no.config.AppConfig
import kartverket.no.generateRiSc.model.CreationRule
import kartverket.no.generateRiSc.model.GcpProjectId
import kartverket.no.generateRiSc.model.InitialRiSc
import kartverket.no.generateRiSc.model.KeyGroup
import kartverket.no.generateRiSc.model.ResourceId
import kartverket.no.generateRiSc.model.RiScContent
import kartverket.no.generateRiSc.model.SopsConfig
import kartverket.no.generateRiSc.model.toRiScCryptoResourceId
import kartverket.no.securityMetrics.SecurityMetricsClientService
import kartverket.no.securityMetrics.SecurityMetricsResponse
import kartverket.no.utils.YamlUtils
import org.slf4j.LoggerFactory

object GenerateRiScService {
    private val config = AppConfig.generateRiScConfig
    private val logger = LoggerFactory.getLogger(GenerateRiScService::class.java)

    suspend fun generateInitialRiSc(
        repositoryName: String,
        gcpProjectId: GcpProjectId,
        publicAgeKey: String?,
    ): InitialRiSc {
        val sopsConfig = generateSopsConfig(gcpProjectId, publicAgeKey)
        val riScContent =
            generateInitialRiScContent(
                SecurityMetricsClientService.fetchSecurityMetrics(repositoryName),
                AirTableClientService.fetchDefaultRiSc(),
            )
        return InitialRiSc(sopsConfig, riScContent)
    }

    private fun generateSopsConfig(
        gcpProjectId: GcpProjectId,
        publicAgeKey: String?,
    ): String =
        YamlUtils.mapToYamlString(
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
                                        gcpKms = listOf(ResourceId(gcpProjectId.toRiScCryptoResourceId())),
                                    ),
                                    KeyGroup(
                                        age = listOf(config.backendPublicKey, config.securityPlatformPublicKey),
                                    ),
                                    publicAgeKey?.let {
                                        KeyGroup(
                                            age = listOf(it),
                                        )
                                    },
                                ),
                        ),
                    ),
            ),
        )

    private fun generateInitialRiScContent(
        securityMetrics: SecurityMetricsResponse,
        defaultRiSc: RiScContent,
    ): String = YamlUtils.mapToYamlString(defaultRiSc)
}
