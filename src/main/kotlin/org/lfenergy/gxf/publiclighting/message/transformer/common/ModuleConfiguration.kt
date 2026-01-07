// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.common

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate

open class ModuleConfiguration {
    fun createJmsListenerContainerFactory(
        connectionFactory: JmsPoolConnectionFactory,
        properties: ModuleConfigurationProperties,
    ) = DefaultJmsListenerContainerFactory().apply {
        setConnectionFactory(connectionFactory)
        setConcurrency("${properties.consumer.minConcurrency}-${properties.consumer.maxConcurrency}")
    }

    fun createJmsTemplate(
        connectionFactory: JmsPoolConnectionFactory,
        properties: ModuleConfigurationProperties,
    ): JmsTemplate =
        JmsTemplate(connectionFactory).apply {
            val qos = properties.producer.qualityOfService
            defaultDestinationName = properties.producer.outboundQueue
            isExplicitQosEnabled = qos.explicitQosEnabled
            if (qos.explicitQosEnabled) {
                setDeliveryPersistent(qos.deliveryPersistent)
                priority = qos.priority
                timeToLive = qos.timeToLive
            }
        }
}
