package kartverket.no.generateRiSc.model

import kotlinx.serialization.Serializable

@Serializable
data class InitialRiSc(
    val sopsConfig: String,
    val initialRiScContent: String,
)
