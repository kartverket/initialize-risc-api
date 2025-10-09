package kartverket.no.descriptor.model

import kartverket.no.generate.model.DefaultRiScType

data class RiScTypeRecordId(
    val riScType: DefaultRiScType,
    val recordId: String,
)
