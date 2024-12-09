package kartverket.no.utils

import io.ktor.server.plugins.requestvalidation.ValidationResult
import kartverket.no.generate.model.GenerateRiScRequestBody
import kartverket.no.generate.model.GenerateSopsConfigRequestBody
import kartverket.no.generate.model.getValidationResult

data class ValidationResultContent(
    val isValid: Boolean,
    val message: String = "",
)

object Validators {
    fun validate(body: GenerateRiScRequestBody): ValidationResult =
        if (body.initialRiSc.isEmpty()) {
            ValidationResult.Invalid("Value of 'initialRiSc' in request body cannot be empty.")
        } else {
            ValidationResult.Valid
        }

    fun validate(body: GenerateSopsConfigRequestBody): ValidationResult {
        val gcpProjectIdValidationResult = body.gcpProjectId.getValidationResult()
        val publicAgeKeysValidationResults = body.publicAgeKeys.map { it.getValidationResult() }
        if (!gcpProjectIdValidationResult.isValid) {
            return ValidationResult.Invalid(gcpProjectIdValidationResult.message)
        } else if (publicAgeKeysValidationResults.any { !it.isValid }) {
            return ValidationResult.Invalid(publicAgeKeysValidationResults.first().message)
        }
        return ValidationResult.Valid
    }
}
