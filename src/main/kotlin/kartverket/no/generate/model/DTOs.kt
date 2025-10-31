@file:Suppress("ktlint:standard:filename")

package kartverket.no.generate.model

import kotlinx.serialization.Serializable

enum class DefaultRiScType {
    Ops,
    InternalJob,
    Standard,
    Begrenset,
}

@Serializable
data class GenerateRiScRequestBody(
    val initialRiSc: String,
    val defaultRiScTypes: List<DefaultRiScType>,
)
