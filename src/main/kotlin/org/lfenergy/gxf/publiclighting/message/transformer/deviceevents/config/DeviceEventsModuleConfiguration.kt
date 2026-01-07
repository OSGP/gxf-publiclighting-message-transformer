// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfiguration
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty("device-events.enabled", havingValue = "true")
@EnableConfigurationProperties(DeviceEventsConfigurationProperties::class)
class DeviceEventsModuleConfiguration(
    private val connectionFactory: JmsPoolConnectionFactory,
    private val properties: DeviceEventsConfigurationProperties,
) : ModuleConfiguration() {
    @Bean("deviceEventsJmsListenerContainerFactory")
    fun jmsListenerContainerFactory() = createJmsListenerContainerFactory(connectionFactory, properties)

    @Bean("deviceEventsJmsTemplate")
    fun jmsTemplate() = createJmsTemplate(connectionFactory, properties)
}
