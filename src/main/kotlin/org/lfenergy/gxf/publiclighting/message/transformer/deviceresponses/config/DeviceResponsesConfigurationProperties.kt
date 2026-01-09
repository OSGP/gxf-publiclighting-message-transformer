// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-responses")
class DeviceResponsesConfigurationProperties(
    consumer: ConsumerProperties,
    producer: ProducerProperties,
) : ModuleConfigurationProperties(consumer, producer)
