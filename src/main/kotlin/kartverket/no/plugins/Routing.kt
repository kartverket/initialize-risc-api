package kartverket.no.plugins

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kartverket.no.generate.generateRiScRoutes

fun Application.configureRouting() {
    routing {
        get("/health") {
            call.respondText("All good!", ContentType.Text.Plain)
        }
        generateRiScRoutes()
    }
}
