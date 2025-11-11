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
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
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
        // Arrange
        val inboundMessage = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)

        // Act
        jmsTemplate.convertAndSend(inboundQueue, inboundMessage.toByteArray())

        // Assert
        val outboundMessage = jmsTemplate.receive(outboundQueue) as ObjectMessage?
        assertThat(outboundMessage).isNotNull()
        assertThat(outboundMessage?.jmsType).isEqualTo(EventType.DEVICE_REGISTRATION.name)
        assertThat(outboundMessage?.`object`).isInstanceOf(DeviceRegistrationDataDto::class.java)
    }

    @Test
    fun `test device notification message processing`() {
        // Arrange
        val inboundMessage = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_NOTIFICATION)

        // Act
        jmsTemplate.convertAndSend(inboundQueue, inboundMessage.toByteArray())

        // Assert
        val outboundMessage = jmsTemplate.receive(outboundQueue) as ObjectMessage?
        assertThat(outboundMessage).isNotNull()
        assertThat(outboundMessage?.jmsType).isEqualTo(EventType.DEVICE_NOTIFICATION.name)
        assertThat(outboundMessage?.`object`).isInstanceOf(EventNotificationDto::class.java)
    }

    @Test
    fun `test unrecognized message processing`() {
        // Arrange
        val inboundMessage = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.UNRECOGNIZED)

        // Act
        jmsTemplate.convertAndSend(inboundQueue, inboundMessage.toByteArray())

        // Assert
        val outboundMessage = jmsTemplate.receive(outboundQueue) as ObjectMessage?
        assertThat(outboundMessage).isNull()
    }
}
