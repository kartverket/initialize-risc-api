package kartverket.no.generateRiSc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RiScContent(
    val schemaVersion: String = "4.0",
    val title: String = "Initiell RoS-vurdering",
    val scope: String =
        "Denne RoS'en er generert fra opplysninger om kodebasen i kartverket.dev, " +
            "sikkerhetsmetrikker og sikkerhetskontrollere. " +
            "DEN ER IKKE KOMPLETT, men et utgangspunkt for teamet til å gjøre sine egne vurderinger.",
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
    @SerialName("ID") val id: String,
    val title: String,
    val actions: List<RiScScenarioAction>,
    val description: String,
    val remainingRisk: Risk,
    val risk: Risk,
    val threatActors: List<String>,
    val vulnerabilities: List<String>,
)

@Serializable
data class Risk(
    val consequence: Int,
    val probability: Int,
)

@Serializable
data class RiScScenarioAction(
    val action: RiScScenarioActionInfo,
    val title: String,
)

@Serializable
data class RiScScenarioActionInfo(
    @SerialName("ID") val id: String,
    val description: String,
    val status: String,
    val url: String,
)
