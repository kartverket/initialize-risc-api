package kartverket.no.utils

import io.ktor.server.plugins.requestvalidation.ValidationResult
import kartverket.no.generate.model.GenerateRiScRequestBody

object Validators {
    fun validate(body: GenerateRiScRequestBody): ValidationResult =
        if (body.initialRiSc.isEmpty()) {
            ValidationResult.Invalid("Value of 'initialRiSc' in request body cannot be empty.")
        } else {
            ValidationResult.Valid
        }
}
