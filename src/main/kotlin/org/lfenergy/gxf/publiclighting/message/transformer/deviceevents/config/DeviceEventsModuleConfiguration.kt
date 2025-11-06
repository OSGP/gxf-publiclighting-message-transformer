// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config

import org.lfenergy.gxf.publiclighting.message.transformer.common.CommonModuleConfiguration
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate

@Configuration
@ConditionalOnProperty("device-events.enabled", havingValue = "true")
@EnableConfigurationProperties(DeviceEventsConfigurationProperties::class)
class DeviceEventsModuleConfiguration(
    private val connectionFactory: JmsPoolConnectionFactory,
    private val properties: DeviceEventsConfigurationProperties,
) {
    @Bean
    fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrency("${properties.consumer.minConcurrency}-${properties.consumer.maxConcurrency}")
        return factory
    }

    @Bean
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
