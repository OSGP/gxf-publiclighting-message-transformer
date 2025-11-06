// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
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
        var whitelist: List<String> = listOf(
            "org.lfenergy.gxf.messaging.model.*",
            "org.lfenergy.gxf.publiclighting.model.*",
            "org.lfenergy.gxf.publiclighting.message.model.*",
            "java.util.*",
            "java.time.*"
        )
    )
}
