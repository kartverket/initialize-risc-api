package kartverket.no.descriptor
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.descriptorRoutes() {
    route("/descriptors") {
        get {
            val descriptors = DescriptorService.getAllRiScDescriptors()
            call.respond(descriptors)
        }
    }
}
