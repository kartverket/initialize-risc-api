package kartverket.no.securityMetrics

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kartverket.no.config.AppConfig
import kartverket.no.entraId.EntraIdClientService
import kartverket.no.entraId.model.ClientId
import kartverket.no.exception.exceptions.HttpClientFetchException
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

object SecurityMetricsClientService {
    private val config = AppConfig.securityMetricsConfig
    private val logger = LoggerFactory.getLogger(SecurityMetricsClientService::class.java)
    private val json = Json { ignoreUnknownKeys = true }
    private val client =
        HttpClient(CIO).config {
            defaultRequest {
                url(config.baseUrl)
            }
        }

    suspend fun fetchSecurityMetrics(repositoryName: String) =
        fetch<SecurityMetricsResponse>("${config.securityMetricsPath}/$repositoryName")

    private suspend inline fun <reified T> fetch(path: String): T {
        val httpResponse =
            client.get {
                url.path(path)
                header(HttpHeaders.Authorization, "Bearer ${EntraIdClientService.getAccessToken(ClientId(config.clientId)).tokenValue}")
            }
        if (httpResponse.status != HttpStatusCode.OK) {
            throw HttpClientFetchException(
                logger,
                "GET request to ${config.baseUrl}/$path failed with status code ${httpResponse.status}",
            )
        }
        return json.decodeFromString<T>(httpResponse.body<String>())
    }
}
