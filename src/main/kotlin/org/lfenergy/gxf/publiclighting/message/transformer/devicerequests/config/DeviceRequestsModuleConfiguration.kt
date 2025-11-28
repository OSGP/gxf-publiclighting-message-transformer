// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate

@Configuration
@ConditionalOnProperty("device-requests.enabled", havingValue = "true")
@EnableConfigurationProperties(DeviceRequestsConfigurationProperties::class)
class DeviceRequestsModuleConfiguration(
    private val connectionFactory: JmsPoolConnectionFactory,
    private val properties: DeviceRequestsConfigurationProperties,
) {
    @Bean("deviceRequestsJmsListenerContainerFactory")
    fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrency("${properties.consumer.minConcurrency}-${properties.consumer.maxConcurrency}")
        return factory
    }

    @Bean("deviceRequestsJmsTemplate")
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
