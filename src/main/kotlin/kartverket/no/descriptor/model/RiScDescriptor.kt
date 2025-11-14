package kartverket.no.descriptor.model

import kartverket.no.generate.model.DefaultRiScType
import kotlinx.serialization.Serializable

@Serializable
data class RiScDescriptor(
    val riScType: DefaultRiScType,
    val listName: String,
    val listDescription: String,
    val defaultTitle: String,
    val defaultScope: String,
    val numberOfScenarios: Int?,
    val numberOfActions: Int?,
)
