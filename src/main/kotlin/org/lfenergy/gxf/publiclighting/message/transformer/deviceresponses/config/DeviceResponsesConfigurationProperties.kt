// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-responses")
data class DeviceResponsesConfigurationProperties(
    var enabled: Boolean,
    var inboundQueue: String,
    var outboundQueue: String,
)
