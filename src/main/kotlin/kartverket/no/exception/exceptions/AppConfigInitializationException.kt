package kartverket.no.exception.exceptions

import org.slf4j.Logger

data class AppConfigInitializationException(
    val fullPath: String,
    val logger: Logger,
    override val message: String = "Encountered NULL when initializing AppConfig with value from $fullPath",
) : Exception() {
    init {
        logger.error(message)
    }
}
