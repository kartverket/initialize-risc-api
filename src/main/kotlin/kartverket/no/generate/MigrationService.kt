package kartverket.no.generate

import kartverket.no.generate.model.BackstageMetadata
import kartverket.no.generate.model.MetadataUnencrypted
import kartverket.no.generate.model.RiScContent

val LATEST_SUPPORTED_VERSION = "5.3"

/**
 * Handles migration of risc content (representing an initial risc) to latest supported version.
 * Migration is only necessary when the RiScs stored in Airtable is not on the latest supported version.
 * Once the airtable risks are updated, migration can be omitted and the code for migration can be deleted.
 */
fun migrateAirtableInitialRiScToNewestVersion(riscContent: RiScContent): RiScContent {
    if (riscContent.schemaVersion == LATEST_SUPPORTED_VERSION) {
        return riscContent
    }

    if (riscContent.schemaVersion == "5.1" || riscContent.schemaVersion == "5.2") {
        return migrateFrom51Or52To53(riscContent)
    }

    throw IllegalArgumentException("The schema version ${riscContent.schemaVersion} is not supported.")
}

fun migrateFrom51Or52To53(riscContent: RiScContent): RiScContent =
    riscContent.copy(
        schemaVersion = "5.3",
        metadata_unencrypted = MetadataUnencrypted(backstage = BackstageMetadata(entityRef = "")),
    )
