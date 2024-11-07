package kartverket.no.generateRiSc.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SopsConfig(
    @JsonProperty("creation_rules") val creationRules: List<CreationRule>,
)

data class CreationRule(
    @JsonProperty("path_regex") val pathRegex: String,
    @JsonProperty("shamir_threshold") val shamirThreshold: Int,
    @JsonProperty("key_groups") val keyGroups: List<KeyGroup>,
)

data class KeyGroup(
    val age: List<String>? = null,
    @JsonProperty("gcp_kms") val gcpKms: List<ResourceId>? = null,
)

data class ResourceId(
    @JsonProperty("resource_id") val resourceId: String,
)
