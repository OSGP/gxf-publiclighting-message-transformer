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
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.ProtobufTestMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceRegistrationDataDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer.DeviceEventMessageSender
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
        every { deviceEventMessageSender.send(any<DeviceEventMessageDto>()) } just Runs

        // Act
        deviceEventMessageListener.onMessage(event)

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

        // Act
        deviceEventMessageListener.onMessage(event)

        // Assert
        assertThat(capturedOutput.out)
            .contains("Received invalid event for device")
            .contains("Unsupported event type")
        verify(exactly = 0) { deviceEventMessageSender.send(any()) }
    }
}
