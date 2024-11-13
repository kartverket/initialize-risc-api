@file:Suppress("ktlint:standard:filename")

package kartverket.no.generateRiSc.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRiScRequestBody(
    val publicAgeKey: String? = null,
    val gcpProjectId: String,
)
