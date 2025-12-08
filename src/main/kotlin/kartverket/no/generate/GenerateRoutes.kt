package kartverket.no.generate

import io.github.smiley4.ktoropenapi.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kartverket.no.generate.model.GenerateRiScRequestBody
import kartverket.no.generate.model.RiScContent
import kartverket.no.utils.Validators
import kotlinx.serialization.json.Json

fun Route.generateRiScRoutes() {
    val json = Json { ignoreUnknownKeys = true }

    install(RequestValidation) {
        validate<GenerateRiScRequestBody> { Validators.validate(it) }
    }

    post("/generate", {
        tags = listOf("Generate")
        request {
            body<GenerateRiScRequestBody> {
                description = "The request body"
                required = true
                example("A valid initial RiSc as JSON") {
                    value =
                        GenerateRiScRequestBody(
                            initialRiSc =
                                """
                                {
                                  "schemaVersion" : "5.1",
                                  "title" : "Super-RoS",
                                  "scope" : "Some super cool description",
                                  "valuations" : [ ],
                                  "scenarios" : [ ]
                                }
                                """.trimIndent(),
                            defaultRiScId = "reczoRP8bUvNiltXg",
                        )
                }
            }
        }
        response {
            code(HttpStatusCode.OK) {
                body<String> {
                    description = "The generated RiSc as JSON"
                }
            }
            code(HttpStatusCode.BadRequest) {}
        }
    }) {
        try {
            val requestBody = call.receive<GenerateRiScRequestBody>()
            val initialRiSc = json.decodeFromString<RiScContent>(requestBody.initialRiSc)
            val defaultRiScId = requestBody.defaultRiScId
            call.respond(
                GenerateService.generateDefaultRiSc(initialRiSc, defaultRiScId),
            )
        } catch (e: RequestValidationException) {
            return@post call.respond(HttpStatusCode.BadRequest, e.reasons.joinToString())
        } catch (_: IllegalArgumentException) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                "Provided value for 'initialRiSc' in request body is not a valid RiSc.",
            )
        }
    }
}
