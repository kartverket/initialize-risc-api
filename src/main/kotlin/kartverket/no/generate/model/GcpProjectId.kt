package kartverket.no.generate.model

import kartverket.no.utils.ValidationResultContent
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class GcpProjectId(
    val value: String,
)

fun GcpProjectId.getValidationResult(): ValidationResultContent =
    if (Regex("^(\\w+-)+\\w+$").matches(value)) {
        ValidationResultContent(true)
    } else {
        ValidationResultContent(false, "Invalid format of GCP Project ID: '$value'")
    }
