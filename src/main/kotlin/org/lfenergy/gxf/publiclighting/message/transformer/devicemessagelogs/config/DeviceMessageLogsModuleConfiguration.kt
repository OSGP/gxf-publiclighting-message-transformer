// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfiguration
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DeviceMessageLogsConfigurationProperties::class)
class DeviceMessageLogsModuleConfiguration(
    private val connectionFactory: JmsPoolConnectionFactory,
    private val properties: DeviceMessageLogsConfigurationProperties,
) : ModuleConfiguration() {
    @Bean("deviceMessageLogsJmsListenerContainerFactory")
    fun jmsListenerContainerFactory() = createJmsListenerContainerFactory(connectionFactory, properties)

    @Bean("deviceMessageLogsJmsTemplate")
    fun jmsTemplate() = createJmsTemplate(connectionFactory, properties)
}
