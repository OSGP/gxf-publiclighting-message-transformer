// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config.DeviceResponsesConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DeviceResponsesConfigurationProperties::class)
class DeviceResponsesModuleConfiguration
