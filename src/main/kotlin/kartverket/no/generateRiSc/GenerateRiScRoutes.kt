package kartverket.no.generateRiSc

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kartverket.no.generateRiSc.model.GcpProjectId
import kartverket.no.generateRiSc.model.GenerateRiScRequestBody

fun Route.generateRiScRoutes() {
    install(RequestValidation) {
        validate<GenerateRiScRequestBody> { body ->
            if (!Regex("^(\\w+-)+\\w+$").matches(body.gcpProjectId)) {
                ValidationResult.Invalid("gcpProjectId is not on the form <team>-<env>-<random alphanumeric sequence>.")
            } else if (body.publicAgeKey != null && !body.publicAgeKey.startsWith("age1")) {
                ValidationResult.Invalid("publicAgeKey, if provided, needs to be on the common form of a public age key.")
            } else {
                ValidationResult.Valid
            }
        }
    }
    route("/generate") {
        post("/{repositoryName}") {
            val repositoryName =
                call.parameters["repositoryName"]
                    ?: return@post call.respond(HttpStatusCode.NotFound, "Expected repository name as path parameter")
            val requestBody =
                try {
                    call.receive<GenerateRiScRequestBody>()
                } catch (e: RequestValidationException) {
                    return@post call.respond(HttpStatusCode.BadRequest, e.reasons.joinToString())
                }
            call.respond(
                GenerateRiScService.generateInitialRiSc(repositoryName, GcpProjectId(requestBody.gcpProjectId), requestBody.publicAgeKey),
            )
        }
    }
}
