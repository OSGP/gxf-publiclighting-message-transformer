// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfigurationProperties.ConsumerProperties
import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfigurationProperties.ProducerProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-message-logs")
class DeviceMessageLogsConfigurationProperties(
    consumer: ConsumerProperties,
    producer: ProducerProperties,
) : ModuleConfigurationProperties(consumer, producer)
