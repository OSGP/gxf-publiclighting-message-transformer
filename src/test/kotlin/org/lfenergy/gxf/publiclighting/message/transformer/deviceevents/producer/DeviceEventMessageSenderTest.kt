// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageDto
import org.springframework.jms.core.JmsTemplate
import java.io.Serializable

@ExtendWith(MockKExtension::class)
class DeviceEventMessageSenderTest {
    @MockK
    lateinit var jmsTemplate: JmsTemplate

    @MockK
    lateinit var properties: DeviceEventsConfigurationProperties

    @InjectMockKs
    lateinit var deviceEventMessageSender: DeviceEventMessageSender

    @Test
    fun `should send object message`() {
        // Arrange
        val message = mockk<DeviceEventMessageDto>()
        val payload = mockk<Serializable>()
        every { properties.producer.outboundQueue } returns OUTBOUND_QUEUE_NAME
        every { message.deviceIdentification } returns "deviceId"
        every { message.messageType } returns "messageType"
        every { message.payload } returns payload
        every { jmsTemplate.convertAndSend(any<String>(), any<Serializable>(), any()) } returns Unit

        // Act
        deviceEventMessageSender.send(message)

        // Assert
        verify { jmsTemplate.convertAndSend(OUTBOUND_QUEUE_NAME, payload, any()) }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "queue"
    }
}
