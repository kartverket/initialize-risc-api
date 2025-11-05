package kartverket.no.config

object AppConfig {
    lateinit var generateRiScConfig: GenerateRiScConfig
    lateinit var airTableConfig: AirTableConfig
}

object AirTableConfig {
    val path: String = "airTable"
    lateinit var baseUrl: String
    lateinit var baseId: String
    lateinit var apiToken: String
    lateinit var tableId: String
    lateinit var recordIdOps: String
    lateinit var recordIdInternalJob: String
    lateinit var recordIdStandard: String
    lateinit var recordIdBegrenset: String
}

object GenerateRiScConfig {
    val path: String = "generateRiSc"
    lateinit var pathRegex: String
}
