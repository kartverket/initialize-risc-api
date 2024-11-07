package kartverket.no.exception.exceptions

import org.slf4j.Logger

data class ReadDefaultRiSCFromResourcesException(
    val logger: Logger,
    override val message: String,
) : Exception() {
    init {
        logger.error(message)
    }
}
