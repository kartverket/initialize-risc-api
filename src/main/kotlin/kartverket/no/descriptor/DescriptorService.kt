package kartverket.no.descriptor

import kartverket.no.airTable.AirTableClientService
import kartverket.no.descriptor.model.RiScDescriptor
import kartverket.no.utils.DefaultRiScTypeUtils

object DescriptorService {
    suspend fun getAllRiScDescriptors(): List<RiScDescriptor> {
        val recordIds = DefaultRiScTypeUtils.getAllRecordIds()
        return AirTableClientService.fetchDefaultRiScDescriptors(recordIds).map {
            it.fields.toRiScDescriptor(DefaultRiScTypeUtils.getRiScTypeFromRecordId(it.id))
        }
    }
}
