package kartverket.no.entraId.model

import java.time.Instant

@JvmInline
value class AccessToken(
    val tokenValue: String,
)

data class EntraIdAccessToken(
    val token: AccessToken,
    val expiresAt: Instant,
) {
    fun isNotExpired() = expiresAt > Instant.now()
}
