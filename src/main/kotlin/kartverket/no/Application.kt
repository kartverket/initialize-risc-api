package kartverket.no

import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.SchemaGenerator.kotlinx
import io.github.smiley4.schemakenerator.swagger.SwaggerSteps
import io.github.smiley4.schemakenerator.swagger.data.TitleType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kartverket.no.config.AirTableConfig
import kartverket.no.config.AppConfig
import kartverket.no.config.GenerateRiScConfig
import kartverket.no.exception.exceptions.AppConfigInitializationException
import kartverket.no.plugins.configureRouting
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

private fun loadConfig(
    config: ApplicationConfig,
    logger: Logger,
) {
    AppConfig.generateRiScConfig =
        GenerateRiScConfig.apply {
            pathRegex =
                config.propertyOrNull("$path.pathRegex")?.getString() ?: throw AppConfigInitializationException("$path.pathRegex", logger)
        }

    AppConfig.airTableConfig =
        AirTableConfig.apply {
            baseUrl = config.propertyOrNull("$path.baseUrl")?.getString()
                ?: throw AppConfigInitializationException(
                    fullPath = "$path.baseUrl",
                    logger = logger,
                )
            baseId = config.propertyOrNull("$path.baseId")?.getString()
                ?: throw AppConfigInitializationException(
                    fullPath = "$path.baseId",
                    logger = logger,
                )
            apiToken = config.propertyOrNull("$path.apiToken")?.getString()
                ?: throw AppConfigInitializationException(
                    fullPath = "$path.apiToken",
                    logger = logger,
                )
            tableId = config.propertyOrNull("$path.tableId")?.getString()
                ?: throw AppConfigInitializationException(
                    fullPath = "$path.tableId",
                    logger = logger,
                )
        }
}

fun Application.module() {
    loadConfig(environment.config, LoggerFactory.getLogger(Application::class.java))
    install(ContentNegotiation) {
        json()
    }
    install(OpenApi) {
        info {
            title = "Kartverket Initialize RiSc API"
            version = "1.0.0"
            description = "API for generating Risk Scorecards from a list of defaults."
        }

        schemas {
            generator =
                kotlinx {
                    nullables = SwaggerSteps.RequiredHandling.NON_REQUIRED
                    optionals = SwaggerSteps.RequiredHandling.REQUIRED
                    title = TitleType.SIMPLE
                    explicitNullTypes = false
                }
        }
    }
    configureRouting()
}
