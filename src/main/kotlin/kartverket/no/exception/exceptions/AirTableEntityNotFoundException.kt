package kartverket.no.exception.exceptions

import org.slf4j.Logger

data class AirTableEntityNotFoundException(
    val logger: Logger,
    override val message: String,
) : Exception() {
    init {
        logger.error(message)
    }
}
