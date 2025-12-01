// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.consumer

import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import jakarta.jms.BytesMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.DeviceEventMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer.DeviceEventMessageSender

@ExtendWith(MockKExtension::class)
class DeviceEventMessageListenerTest {
    @MockK
    lateinit var deviceEventMessageSender: DeviceEventMessageSender

    @InjectMockKs
    lateinit var deviceEventMessageListener: DeviceEventMessageListener

    @Test
    fun `should receive protobuf event and send dto`() {
        // Arrange
        val event = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)
        val bytesMessage = setupBytesMessageMock(event)
        every { deviceEventMessageSender.send(any<DeviceEventMessage>()) } just Runs

        // Act
        deviceEventMessageListener.onMessage(bytesMessage)

        // Assert
        verify {
            deviceEventMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceEventMessage::class.java)
                    assertThat(it.hasDeviceRegistrationReceivedEvent()).isTrue
                    assertThat(it.deviceRegistrationReceivedEvent).isNotNull
                },
            )
        }
    }

    private fun setupBytesMessageMock(deviceEventMessage: DeviceEventMessage): BytesMessage {
        val bytesMessage = mockk<BytesMessage>()
        val bytes = deviceEventMessage.toByteArray()
        every { bytesMessage.jmsCorrelationID } returns deviceEventMessage.header.correlationUid
        every { bytesMessage.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION) } returns deviceEventMessage.header.deviceIdentification
        every { bytesMessage.jmsType } returns deviceEventMessage.header.eventType.name
        every { bytesMessage.bodyLength } returns bytes.size.toLong()
        every { bytesMessage.readBytes(any<ByteArray>()) } answers {
            val buffer = arg<ByteArray>(0)
            System.arraycopy(bytes, 0, buffer, 0, bytes.size)
            bytes.size
        }
        return bytesMessage
    }
}
