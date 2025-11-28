// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-requests")
class DeviceRequestsConfigurationProperties(
    enabled: Boolean,
    consumer: ConsumerProperties,
    producer: ProducerProperties,
) : ModuleConfigurationProperties(enabled, consumer, producer)
