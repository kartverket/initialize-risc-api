@file:Suppress("ktlint:standard:filename")

package kartverket.no.generate.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRiScRequestBody(
    val initialRiSc: String,
    val defaultRiScId: String,
)
