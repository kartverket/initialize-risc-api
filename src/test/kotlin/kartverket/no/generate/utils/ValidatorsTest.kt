package kartverket.no.generate.utils

import io.ktor.server.plugins.requestvalidation.ValidationResult
import kartverket.no.generate.model.GenerateRiScRequestBody
import kartverket.no.utils.Validators
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidatorsTest {
    @Test
    fun `should return invalid when initialRiSc is empty`() {
        val result = Validators.validate(GenerateRiScRequestBody(""))

        assertTrue(result is ValidationResult.Invalid)
        assertTrue("Value of 'initialRiSc' in request body cannot be empty." in result.reasons)
    }

    @Test
    fun `should return valid when initialRiSc is not empty`() {
        val result = Validators.validate(GenerateRiScRequestBody("{ \"not\": \"empty\" }"))
        assertEquals(ValidationResult.Valid, result)
    }
}
