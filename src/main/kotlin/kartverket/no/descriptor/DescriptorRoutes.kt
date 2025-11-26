package kartverket.no.descriptor
import io.github.smiley4.ktoropenapi.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kartverket.no.descriptor.model.RiScDescriptor

fun Route.descriptorRoutes() {
    get("/descriptors", {
        tags = listOf("Descriptors")
        response {
            code(HttpStatusCode.OK) {
                body<List<RiScDescriptor>>()
            }
        }
    }) {
        val descriptors = DescriptorService.getAllRiScDescriptors()
        call.respond(descriptors)
    }
}
