package kartverket.no.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object YamlUtils {
    val yamlFactory = YAMLFactory()

    fun <T> mapToYamlString(t: T) =
        ObjectMapper(yamlFactory.enable(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE))
            .setSerializationInclusion(
                JsonInclude.Include.NON_NULL,
            ).registerKotlinModule()
            .writeValueAsString(t)
}
