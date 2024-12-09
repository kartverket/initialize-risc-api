@file:Suppress("ktlint:standard:filename")

package kartverket.no.generate.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRiScRequestBody(
    val initialRiSc: String,
)

@Serializable
data class GenerateSopsConfigRequestBody(
    val gcpCryptoKey: GcpCryptoKeyObject,
    val publicAgeKeys: List<PublicAgeKey>,
)

@Serializable
data class GcpCryptoKeyObject(
    val projectId: GcpProjectId,
    val keyRing: String,
    val name: String,
)

fun GcpCryptoKeyObject.toResourceId() = "projects/${projectId.value}/locations/europe-north1/keyRings/$keyRing/cryptoKeys/$name"
