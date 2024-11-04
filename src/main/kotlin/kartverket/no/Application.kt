package kartverket.no

import io.ktor.server.application.*
import kartverket.no.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
}
