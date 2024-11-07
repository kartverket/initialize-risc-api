package kartverket.no.generateRiSc.model

@JvmInline
value class GcpProjectId(
    val value: String,
)

fun GcpProjectId.toRiScCryptoResourceId() =
    "projects/${this.value}/locations/europe-north1/keyRings" +
        "/${this.value.substringBeforeLast("-")}-risc-key-ring"
