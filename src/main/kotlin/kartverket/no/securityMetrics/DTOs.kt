package kartverket.no.securityMetrics

import kartverket.no.utils.OffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class SecurityMetricsResponse(
    val repositoryName: String,
    val metadata: RepositoryMetrics,
    val dependabot: DependabotMetrics,
    val codeScanning: CodeScanningMetrics,
    val secretScanning: SecretScanningMetrics,
    val sysdig: List<SysdigMetrics>?,
)

@Serializable
data class VulnerabilityCount(
    val low: Int,
    val medium: Int,
    val high: Int,
    val critical: Int,
)

@Serializable
data class DependabotMetrics(
    val configuredDependabot: Boolean,
    val vulnerabilities: VulnerabilityCount,
)

@Serializable
data class CodeScanningConfiguration(
    val configuredGithubAdvancedSecurity: Boolean,
    val configuredCodeQl: Boolean,
    val configuredPharos: Boolean,
    val configuredOtherTool: Boolean,
)

@Serializable
data class CodeScanningMetrics(
    val configuration: CodeScanningConfiguration,
    val vulnerabilities: VulnerabilityCount,
)

@Serializable
data class SysdigMetrics(
    val containerName: String,
    val cluster: Cluster,
    val vulnerabilities: VulnerabilityCount,
)

@Serializable
enum class Cluster {
    @SerialName("atkv1-dev")
    ATKV1_DEV,

    @SerialName("atkv3-dev")
    ATKV3_DEV,

    @SerialName("atgcp1-dev")
    ATGCP1_DEV,

    @SerialName("atkv1-test")
    ATKV1_TEST,

    @SerialName("atkv3-test")
    ATKV3_TEST,

    @SerialName("atgcp1-test")
    ATGCP1_TEST,

    @SerialName("atkv1-prod")
    ATKV1_PROD,

    @SerialName("atkv3-prod")
    ATKV3_PROD,

    @SerialName("atgcp1-prod")
    ATGCP1_PROD,
}

@Serializable
data class SecretScanningMetrics(
    val count: Int = 0,
)

@Serializable
data class RepositoryMetrics(
    val visibility: String,
    val hasSecChamp: Boolean,
    val primaryLanguage: String,
    val size: Int,
    @Serializable(with = OffsetDateTimeSerializer::class) val lastCommitOnMain: OffsetDateTime,
    val codeOwners: CodeOwnersMetrics,
    val repoTypes: String,
    val platform: String,
)

@Serializable
data class CodeOwnersMetrics(
    val hasCodeOwners: Boolean,
    val hasErrors: Boolean,
)
