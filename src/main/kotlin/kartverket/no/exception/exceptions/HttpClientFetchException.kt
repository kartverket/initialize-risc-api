package kartverket.no.exception.exceptions

import org.slf4j.Logger

data class HttpClientFetchException(
    val logger: Logger,
    override val message: String,
) : Exception() {
    init {
        logger.error(message)
    }
}
