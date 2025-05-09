package kartverket.no

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kartverket.no.config.AirTableConfig
import kartverket.no.config.AppConfig
import kartverket.no.config.EntraIdConfig
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
            backendPublicKey =
                config.propertyOrNull("$path.backendPublicKey")?.getString()
                    ?: throw AppConfigInitializationException(
                        fullPath = "$path.backendPublicKey",
                        logger = logger,
                    )
            securityPlatformPublicKey =
                config.propertyOrNull("$path.securityPlatformPublicKey")?.getString()
                    ?: throw AppConfigInitializationException(
                        fullPath = "$path.securityPlatformPublicKey",
                        logger = logger,
                    )
            securityTeamPublicKey =
                config.propertyOrNull("$path.securityTeamPublicKey")?.getString()
                    ?: throw AppConfigInitializationException(
                        fullPath = "$path.securityTeamPublicKey",
                        logger = logger,
                    )
        }

    AppConfig.entraIdConfig =
        EntraIdConfig.apply {
            baseUrl = config.propertyOrNull("$path.baseUrl")?.getString()
                ?: "https://baseurl.dummy.com"
            tenantId =
                config.propertyOrNull("$path.tenantId")?.getString()
                    ?: "dummy-tenant-id"
            clientId =
                config.propertyOrNull("$path.clientId")?.getString()
                    ?: "dummy-client-id"
            clientSecret =
                config.propertyOrNull("$path.clientSecret")?.getString()
                    ?: "dummy-secret"
            tokenUrl = "$baseUrl/$tenantId/oauth2/v2.0/token"
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
            recordId = config.propertyOrNull("$path.recordId")?.getString()
                ?: throw AppConfigInitializationException(
                    fullPath = "$path.recordId",
                    logger = logger,
                )
        }
}

fun Application.module() {
    loadConfig(environment.config, LoggerFactory.getLogger(Application::class.java))
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}
