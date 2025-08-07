package kartverket.no.generate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RiScContent(
    val schemaVersion: String,
    val title: String,
    val scope: String,
    val valuations: List<RiScValuation>,
    val scenarios: List<RiScScenario>,
)

@Serializable
data class RiScValuation(
    val description: String,
    val confidentiality: String,
    val integrity: String,
    val availability: String,
)

@Serializable
data class RiScScenario(
    val title: String,
    val scenario: Scenario,
)

@Serializable
data class Scenario(
    @SerialName("ID") val id: String,
    val description: String,
    val threatActors: List<ThreatActor>,
    val vulnerabilities: List<Vulnerability>,
    val risk: Risk,
    val actions: List<RiScScenarioAction>,
    val remainingRisk: Risk,
)

@Serializable
enum class ThreatActor {
    @SerialName("Script kiddie")
    SCRIPT_KIDDIE,

    @SerialName("Hacktivist")
    HACKTIVIST,

    @SerialName("Reckless employee")
    RECKLESS_EMPLOYEE,

    @SerialName("Insider")
    INSIDER,

    @SerialName("Organised crime")
    ORGANIZED_CRIME,

    @SerialName("Terrorist organisation")
    TERRORIST_ORGANIZATION,

    @SerialName("Nation/government")
    NATION_GOVERNMENT,
}

@Serializable
enum class Vulnerability {
    @SerialName("Flawed design")
    FLAWED_DESIGN,

    @SerialName("Misconfiguration")
    MISCONFIGURATION,

    @SerialName("Dependency vulnerability")
    DEPENDENCY_VULNERABILITY,

    @SerialName("Unauthorized access")
    UNAUTHORIZED_ACCESS,

    @SerialName("Unmonitored use")
    UNMONITORED_USE,

    @SerialName("Input tampering")
    INPUT_TAMPERING,

    @SerialName("Information leak")
    INFORMATION_LEAK,

    @SerialName("Excessive use")
    EXCESSIVE_USE,
}

@Serializable
data class Risk(
    val consequence: Int,
    val probability: Float,
)

@Serializable
data class RiScScenarioAction(
    val title: String,
    val action: RiScScenarioActionInfo,
)

@Serializable
data class RiScScenarioActionInfo(
    @SerialName("ID") val id: String,
    val description: String,
    val status: RiScScenarioActionStatus,
    val url: String,
)

@Serializable
enum class RiScScenarioActionStatus {
    @SerialName("OK")
    OK,

    @SerialName("Not OK")
    NOT_OK,

    @SerialName("Not relevant")
    NOT_RELEVANT,
}