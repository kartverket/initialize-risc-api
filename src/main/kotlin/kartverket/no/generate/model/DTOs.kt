@file:Suppress("ktlint:standard:filename")

package kartverket.no.generate.model

import kotlinx.serialization.Serializable

enum class DefaultRiScType {
    Ops,
    InternalJob,
    Standard,
}

@Serializable
data class GenerateRiScRequestBody(
    val initialRiSc: String,
    val defaultRiScId: String,
)
