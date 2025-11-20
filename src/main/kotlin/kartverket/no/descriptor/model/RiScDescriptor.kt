package kartverket.no.descriptor.model

import kotlinx.serialization.Serializable

@Serializable
data class RiScDescriptor(
    val id: String,
    val listName: String,
    val listDescription: String,
    val defaultTitle: String,
    val defaultScope: String,
    val numberOfScenarios: Int?,
    val numberOfActions: Int?,
    val preferredBackstageComponentType: String?,
    val priorityIndex: Int?,
)
