// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DeviceResponsesConfigurationProperties::class)
class DeviceResponsesModuleConfiguration(
    private val connectionFactory: org.messaginghub.pooled.jms.JmsPoolConnectionFactory,
    private val properties: DeviceResponsesConfigurationProperties,
) : ModuleConfiguration() {
    @Bean("deviceResponsesJmsListenerContainerFactory")
    fun jmsListenerContainerFactory() = createJmsListenerContainerFactory(connectionFactory, properties)

    @Bean("deviceResponsesJmsTemplate")
    fun jmsTemplate() = createJmsTemplate(connectionFactory, properties)
}
