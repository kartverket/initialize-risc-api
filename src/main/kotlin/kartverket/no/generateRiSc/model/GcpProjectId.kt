package kartverket.no.generateRiSc.model

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
