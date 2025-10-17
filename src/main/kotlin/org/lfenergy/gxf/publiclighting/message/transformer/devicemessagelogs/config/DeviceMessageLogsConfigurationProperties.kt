// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-message-logs")
data class DeviceMessageLogsConfigurationProperties(
    var enabled: Boolean,
    var inboundQueue: String,
    var outboundQueue: String,
)
