package kartverket.no.generate.model

import kartverket.no.utils.ValidationResultContent
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class GcpProjectId(
    val value: String,
)

private fun GcpProjectId.removeLastTwoSegments() =
    this.value
        .split('-')
        .dropLast(2)
        .joinToString("-")

fun GcpProjectId.toRiScCryptoResourceId() =
    "projects/${this.value}/locations/europe-north1/keyRings" +
        "/${this.removeLastTwoSegments()}-risc-key-ring" +
        "/cryptoKeys/${this.removeLastTwoSegments()}-risc-crypto-key"

fun GcpProjectId.getValidationResult(): ValidationResultContent =
    if (Regex("^(\\w+-)+\\w+$").matches(value)) {
        ValidationResultContent(true)
    } else {
        ValidationResultContent(false, "Invalid format of GCP Project ID: '$value'")
    }
