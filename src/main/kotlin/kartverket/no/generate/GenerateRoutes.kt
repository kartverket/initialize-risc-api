package kartverket.no.generate

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kartverket.no.generate.model.GenerateRiScRequestBody
import kartverket.no.generate.model.RiScContent
import kartverket.no.utils.Validators
import kotlinx.serialization.json.Json

fun Route.generateRiScRoutes() {
    val json = Json { ignoreUnknownKeys = true }

    install(RequestValidation) {
        validate<GenerateRiScRequestBody> { Validators.validate(it) }
    }

    route("/generate") {
        post {
            try {
                val requestBody = call.receive<GenerateRiScRequestBody>()
                val initialRiSc = json.decodeFromString<RiScContent>(requestBody.initialRiSc)
                val defaultRiScTypes = requestBody.defaultRiScTypes
                call.respond(
                    GenerateService.generateDefaultRiSc(initialRiSc, defaultRiScTypes),
                )
            } catch (e: RequestValidationException) {
                return@post call.respond(HttpStatusCode.BadRequest, e.reasons.joinToString())
            } catch (e: IllegalArgumentException) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Provided value for 'initialRiSc' in request body is not a valid RiSc.",
                )
            }
        }
    }
}
