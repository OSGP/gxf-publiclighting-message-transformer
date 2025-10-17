// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents

import jakarta.annotation.PostConstruct
import jakarta.jms.ObjectMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.ArtemisContainerConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.ProtobufTestMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceRegistrationDataDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.EventNotificationDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.jms.core.JmsTemplate

@SpringBootTest
@ImportTestcontainers(ArtemisContainerConfiguration::class)
class DeviceEventsIntegrationTest {
    @Autowired
    lateinit var jmsTemplate: JmsTemplate

    @Autowired
    lateinit var properties: DeviceEventsConfigurationProperties

    private lateinit var inboundQueue: String
    private lateinit var outboundQueue: String

    @PostConstruct
    fun setup() {
        jmsTemplate.receiveTimeout = 2000
        inboundQueue = properties.consumer.inboundQueue
        outboundQueue = properties.producer.outboundQueue
    }

    @Test
    fun `test device registration message processing`() {
        // Given
        val inboundMessage = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)

        // When
        jmsTemplate.convertAndSend(inboundQueue, inboundMessage)

        // Then
        val outboundMessage = jmsTemplate.receive(outboundQueue) as ObjectMessage?
        assertThat(outboundMessage).isNotNull()
        assertThat(outboundMessage?.jmsType).isEqualTo(EventType.DEVICE_REGISTRATION.name)
        assertThat(outboundMessage?.`object`).isInstanceOf(DeviceRegistrationDataDto::class.java)
    }

    @Test
    fun `test device notification message processing`() {
        // Given
        val inboundMessage = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_NOTIFICATION)

        // When
        jmsTemplate.convertAndSend(inboundQueue, inboundMessage)

        // Then
        val outboundMessage = jmsTemplate.receive(outboundQueue) as ObjectMessage?
        assertThat(outboundMessage).isNotNull()
        assertThat(outboundMessage?.jmsType).isEqualTo(EventType.DEVICE_NOTIFICATION.name)
        assertThat(outboundMessage?.`object`).isInstanceOf(EventNotificationDto::class.java)
    }

    @Test
    fun `test unrecognized message processing`() {
        // Given
        val inboundMessage = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.UNRECOGNIZED)

        // When
        jmsTemplate.convertAndSend(inboundQueue, inboundMessage)

        // Then
        val outboundMessage = jmsTemplate.receive(outboundQueue) as ObjectMessage?
        assertThat(outboundMessage).isNull()
    }
}
