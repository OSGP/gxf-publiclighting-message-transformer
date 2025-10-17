package org.lfenergy.gxf.publiclighting.message.transformer.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "common")
data class CommonConfigurationProperties(
    var messaging: MessagingConfigurationProperties,
) {
    data class MessagingConfigurationProperties(
        var deserializationPolicy: DeserializationPolicy,
    )

    data class DeserializationPolicy(
        var whitelist: List<String> = emptyList(),
    )
}
