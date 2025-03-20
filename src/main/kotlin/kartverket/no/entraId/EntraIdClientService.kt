package kartverket.no.entraId

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kartverket.no.config.AppConfig
import kartverket.no.entraId.model.AccessToken
import kartverket.no.entraId.model.ClientCredentialsFlowResponse
import kartverket.no.entraId.model.ClientId
import kartverket.no.entraId.model.EntraIdAccessToken
import kartverket.no.exception.exceptions.HttpClientFetchException
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

object EntraIdClientService {
    private val logger = LoggerFactory.getLogger(EntraIdClientService::class.java)
    private val accessToken: AtomicReference<EntraIdAccessToken> = AtomicReference()
    private val entraIdConfig = AppConfig.entraIdConfig
    private val json = Json { ignoreUnknownKeys = true }
    private val client =
        HttpClient(CIO) {
            defaultRequest {
                url(entraIdConfig.tokenUrl)
            }
        }

    suspend fun getAccessToken(audience: ClientId): AccessToken {
        return if (accessToken.get() != null && accessToken.get().isNotExpired()) {
            AccessToken(accessToken.get().token.tokenValue)
        } else {
            val accessTokenResponse = fetchAccessToken(audience = audience)
            val newAccessToken = AccessToken(accessTokenResponse.accessToken)
            accessToken.set(
                EntraIdAccessToken(
                    token = newAccessToken,
                    expiresAt = Instant.now().plusSeconds(accessTokenResponse.expiresIn.toLong()),
                ),
            )
            return newAccessToken
        }
    }

    private suspend fun fetchAccessToken(audience: ClientId): ClientCredentialsFlowResponse {
        val httpResponse =
            try {
                client.submitForm(
                    formParameters =
                        parameters {
                            append("grant_type", "client_credentials")
                            append("client_id", entraIdConfig.clientId)
                            append("client_secret", entraIdConfig.clientSecret)
                            append("scope", "${audience.value}/.default")
                        },
                )
            } catch (e: Exception) {
                throw HttpClientFetchException(
                    logger,
                    "POST request to retrieve access token with client credentials flow " +
                        "failed due to the following exception: ${e.message}",
                )
            }
        val body = json.decodeFromString<ClientCredentialsFlowResponse>(httpResponse.body<String>())
        return body
    }
}
