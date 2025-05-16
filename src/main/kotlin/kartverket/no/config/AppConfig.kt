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
    lateinit var recordId: String
    lateinit var tableId: String
}

object GenerateRiScConfig {
    val path: String = "generateRiSc"
    lateinit var pathRegex: String
    lateinit var backendPublicKey: String
    lateinit var securityPlatformPublicKey: String
    lateinit var securityTeamPublicKey: String
}
