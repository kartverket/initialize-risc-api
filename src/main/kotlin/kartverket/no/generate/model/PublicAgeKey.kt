package kartverket.no.generate.model

import kartverket.no.utils.ValidationResultContent
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class PublicAgeKey(
    val value: String,
)

fun PublicAgeKey.getValidationResult(): ValidationResultContent {
    if (value.length != 62) {
        return ValidationResultContent(false, "Public age key: '$value' is not 62 characters long")
    }

    if (!value.startsWith("age1")) {
        return ValidationResultContent(false, "Public age key: '$value' does not start with 'age1'")
    }

    return if (Regex("^age1[ac-hj-np-z02-9]{58}$").matches(value)) {
        ValidationResultContent(true)
    } else {
        ValidationResultContent(false, "The part after 'age1' in '$value' is not base58-formatted")
    }
}
