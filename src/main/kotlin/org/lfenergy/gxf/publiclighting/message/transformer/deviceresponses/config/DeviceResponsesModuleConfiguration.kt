// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate

@Configuration
@EnableConfigurationProperties(DeviceResponsesConfigurationProperties::class)
class DeviceResponsesModuleConfiguration(
    private val connectionFactory: org.messaginghub.pooled.jms.JmsPoolConnectionFactory,
    private val properties: DeviceResponsesConfigurationProperties,
) {
    @Bean("deviceResponsesJmsListenerContainerFactory")
    fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrency("${properties.consumer.minConcurrency}-${properties.consumer.maxConcurrency}")
        return factory
    }

    @Bean("deviceResponsesJmsTemplate")
    fun jmsTemplate(): JmsTemplate {
        val template = JmsTemplate(connectionFactory)
        template.defaultDestinationName = properties.producer.outboundQueue
        with(properties.producer.qualityOfService) {
            if (explicitQosEnabled) {
                template.isExplicitQosEnabled = explicitQosEnabled
                template.setDeliveryPersistent(deliveryPersistent)
                template.priority = priority
                template.timeToLive = timeToLive
            }
        }
        return template
    }
}
