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
import kartverket.no.config.SecuirtyMetricsConfig
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
                    ?: throw AppConfigInitializationException("$path.backendPublicKey", logger)
            securityPlatformPublicKey =
                config.propertyOrNull("$path.securityPlatformPublicKey")?.getString()
                    ?: throw AppConfigInitializationException("$path.securityPlatformPublicKey", logger)
            securityTeamPublicKey =
                config.propertyOrNull("$path.securityTeamPublicKey")?.getString()
                    ?: throw AppConfigInitializationException("$path.securityTeamPublicKey", logger)
        }

    AppConfig.securityMetricsConfig =
        SecuirtyMetricsConfig.apply {
            baseUrl = config.propertyOrNull("$path.baseUrl")?.getString()
                ?: throw AppConfigInitializationException("$path.baseUrl", logger)
            securityMetricsPath = config.propertyOrNull("$path.securityMetricsPath")?.getString()
                ?: throw AppConfigInitializationException("$path.securityMetricsPath", logger)
            clientId = config.propertyOrNull("$path.clientId")?.getString()
                ?: throw AppConfigInitializationException("$path.clientId", logger)
        }

    AppConfig.entraIdConfig =
        EntraIdConfig.apply {
            baseUrl = config.propertyOrNull("$path.baseUrl")?.getString()
                ?: throw AppConfigInitializationException("$path.baseUrl", logger)
            tenantId =
                config.propertyOrNull("$path.tenantId")?.getString() ?: throw AppConfigInitializationException("$path.tenantId", logger)
            clientId =
                config.propertyOrNull("$path.clientId")?.getString() ?: throw AppConfigInitializationException("$path.clientId", logger)
            clientSecret =
                config.propertyOrNull("$path.clientSecret")?.getString()
                    ?: throw AppConfigInitializationException("$path.clientSecret", logger)
            tokenUrl = "$baseUrl/$tenantId/oauth2/v2.0/token"
        }

    AppConfig.airTableConfig =
        AirTableConfig.apply {
            baseUrl = config.propertyOrNull("$path.baseUrl")?.getString()
                ?: throw AppConfigInitializationException("$path.baseUrl", logger)
            baseId = config.propertyOrNull("$path.baseId")?.getString()
                ?: throw AppConfigInitializationException("$path.baseId", logger)
            apiToken = config.propertyOrNull("$path.apiToken")?.getString()
                ?: throw AppConfigInitializationException("$path.apiToken", logger)
            recordId = config.propertyOrNull("$path.recordId")?.getString()
                ?: throw AppConfigInitializationException("$path.recordId", logger)
        }
}

fun Application.module() {
    loadConfig(environment.config, LoggerFactory.getLogger(Application::class.java))
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}
