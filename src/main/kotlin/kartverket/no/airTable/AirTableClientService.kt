package kartverket.no.airTable

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kartverket.no.airTable.model.AirTableFetchRecordsResponse
import kartverket.no.config.AppConfig
import kartverket.no.exception.exceptions.HttpClientFetchException
import kartverket.no.generate.model.DefaultRiScType
import kartverket.no.generate.model.RiScContent
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

object AirTableClientService {
    private val config = AppConfig.airTableConfig
    private val logger = LoggerFactory.getLogger(AirTableClientService::class.java)
    private val json = Json { ignoreUnknownKeys = true }
    private val client =
        HttpClient(CIO).config {
            defaultRequest {
                url(config.baseUrl)
            }
        }

    suspend fun fetchDefaultRiSc(defaultRiScType: DefaultRiScType): RiScContent {
        val recordId =
            when (defaultRiScType) {
                DefaultRiScType.Ops -> config.recordIdOps
                DefaultRiScType.InternalJob -> config.recordIdInternalJob
                DefaultRiScType.Standard -> config.recordIdStandard
            }

        val riScContent =
            json.decodeFromString<RiScContent>(
                fetch<AirTableFetchRecordsResponse>(
                    path = "v0/${config.baseId}/${config.tableId}",
                    queryParams = mapOf("view" to "RoS-Json"),
                ).toRiScContentString(logger, recordId),
            )
        return riScContent
    }

    private suspend inline fun <reified T> fetch(
        path: String,
        queryParams: Map<String, String> = emptyMap(),
    ): T {
        val httpResponse =
            client.get {
                url.path(path)
                header(HttpHeaders.Authorization, "Bearer ${config.apiToken}")
                queryParams.forEach { (key, value) ->
                    url.parameters.append(key, value)
                }
            }
        if (httpResponse.status != HttpStatusCode.OK) {
            throw HttpClientFetchException(
                logger,
                "GET request to ${config.baseUrl}/$path failed with status code ${httpResponse.status}",
            )
        }
        val response = json.decodeFromString<T>(httpResponse.body<String>())
        return response
    }
}
