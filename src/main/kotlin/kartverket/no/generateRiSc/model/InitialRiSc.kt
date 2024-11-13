package kartverket.no.generateRiSc.model

import kotlinx.serialization.Serializable

@Serializable
data class InitialRiSc(
    val sopsConfig: String,
    val schemaVersion: String,
    val initialRiScContent: String,
    val userInfo: UserInfo,
)

@Serializable
data class UserInfo(
    val name: String,
    val email: String,
)
