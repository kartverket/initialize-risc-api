@file:Suppress("ktlint:standard:filename")

package kartverket.no.entraId.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientCredentialsFlowResponse(
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("access_token") val accessToken: String,
)
