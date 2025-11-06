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
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.ProtobufTestMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer.DeviceEventMessageSender
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension

@ExtendWith(MockKExtension::class)
@ExtendWith(OutputCaptureExtension::class)
class DeviceEventMessageListenerTest {
    @MockK
    lateinit var deviceEventMessageSender: DeviceEventMessageSender

    @InjectMockKs
    lateinit var deviceEventMessageListener: DeviceEventMessageListener

    @Test
    fun `should receive protobuf event and send dto`() {
        // Arrange
        val event = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)
        val bytesMessage = setupBytesMessageMock(event.toByteArray())
        every { deviceEventMessageSender.send(any<DeviceEventMessageDto>()) } just Runs

        // Act
        deviceEventMessageListener.onMessage(bytesMessage)

        // Assert
        verify {
            deviceEventMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceEventMessageDto::class.java)
                    assertThat(it.payload).isInstanceOf(DeviceRegistrationDataDto::class.java)
                },
            )
        }
    }

    @Test
    fun `should log unrecognized protobuf event and not send dto`(capturedOutput: CapturedOutput) {
        // Arrange
        val event = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.UNRECOGNIZED)
        val bytesMessage = setupBytesMessageMock(event.toByteArray())

        // Act
        deviceEventMessageListener.onMessage(bytesMessage)

        // Assert
        assertThat(capturedOutput.out)
            .contains("Received invalid event for device")
            .contains("Unsupported event type")
        verify(exactly = 0) { deviceEventMessageSender.send(any()) }
    }

    private fun setupBytesMessageMock(bytes: ByteArray): BytesMessage {
        val bytesMessage = mockk<BytesMessage>()
        every { bytesMessage.bodyLength } returns bytes.size.toLong()
        every { bytesMessage.readBytes(any<ByteArray>()) } answers {
            val buffer = arg<ByteArray>(0)
            System.arraycopy(bytes, 0, buffer, 0, bytes.size)
            bytes.size
        }
        return bytesMessage
    }
}
