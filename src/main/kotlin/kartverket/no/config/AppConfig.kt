package kartverket.no.config

object AppConfig {
    lateinit var generateRiScConfig: GenerateRiScConfig
    lateinit var securityMetricsConfig: SecuirtyMetricsConfig
    lateinit var entraIdConfig: EntraIdConfig
    lateinit var airTableConfig: AirTableConfig
}

object AirTableConfig {
    val path: String = "airTable"
    lateinit var baseUrl: String
    lateinit var baseId: String
    lateinit var apiToken: String
    lateinit var recordId: String
}

object GenerateRiScConfig {
    val path: String = "generateRiSc"
    lateinit var pathRegex: String
    lateinit var backendPublicKey: String
    lateinit var securityPlatformPublicKey: String
    lateinit var securityTeamPublicKey: String
}

object SecuirtyMetricsConfig {
    val path: String = "securityMetrics"
    lateinit var baseUrl: String
    lateinit var securityMetricsPath: String
    lateinit var clientId: String
}

object EntraIdConfig {
    val path: String = "entraId"
    lateinit var baseUrl: String
    lateinit var tenantId: String
    lateinit var clientId: String
    lateinit var clientSecret: String
    lateinit var tokenUrl: String
}
