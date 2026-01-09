// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.ModuleConfiguration
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DeviceRequestsConfigurationProperties::class)
class DeviceRequestsModuleConfiguration(
    private val connectionFactory: JmsPoolConnectionFactory,
    private val properties: DeviceRequestsConfigurationProperties,
) : ModuleConfiguration() {
    @Bean("deviceRequestsJmsListenerContainerFactory")
    fun jmsListenerContainerFactory() = createJmsListenerContainerFactory(connectionFactory, properties)

    @Bean("deviceRequestsJmsTemplate")
    fun jmsTemplate() = this.createJmsTemplate(connectionFactory, properties)
}
