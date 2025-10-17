// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents

import org.lfenergy.gxf.publiclighting.message.transformer.common.CommonModuleConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate

@Configuration
@EnableConfigurationProperties(DeviceEventsConfigurationProperties::class)
@Import(CommonModuleConfiguration::class)
class DeviceEventsModuleConfiguration(
    private val connectionFactory: JmsPoolConnectionFactory,
    private val properties: DeviceEventsConfigurationProperties,
) {
    // TODO: replace hard coded values with properties as needed
    @Bean
    fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrency("${properties.consumer.minConcurrency}-${properties.consumer.maxConcurrency}")
        factory.setSessionTransacted(true)
        // TODO: implement error handling as needed
        factory.setErrorHandler { /* handle error, e.g. log */ }
        factory.setRecoveryInterval(1000L)
        factory.setMaxMessagesPerTask(10)
        return factory
    }

    @Bean
    fun jmsTemplate(): JmsTemplate {
        val template = JmsTemplate(connectionFactory)
        template.defaultDestinationName = properties.producer.outboundQueue
        with(properties.producer.qualityOfService) {
            template.isExplicitQosEnabled = explicitQosEnabled
            template.setDeliveryPersistent(deliveryPersistent)
            template.priority = priority
            template.timeToLive = timeToLive
        }
        return template
    }
}
