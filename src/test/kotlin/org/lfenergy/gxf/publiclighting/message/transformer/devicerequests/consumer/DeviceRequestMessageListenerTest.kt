// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.consumer

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
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer.DeviceRequestMessageSender
import org.springframework.boot.test.system.OutputCaptureExtension
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestObjectMessageMockFactory as MockFactory

@ExtendWith(MockKExtension::class)
@ExtendWith(OutputCaptureExtension::class)
class DeviceRequestMessageListenerTest {
    @MockK
    lateinit var deviceRequestMessageSender: DeviceRequestMessageSender

    @InjectMockKs
    lateinit var deviceRequestMessageListener: DeviceRequestMessageListener

    @Test
    fun `should handle set light device request message`() {
        // Arrange
        val message = MockFactory.deviceRequestObjectMessageMock(MockFactory.REQUEST_TYPE_SET_LIGHT)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        // Act
        deviceRequestMessageListener.onMessage(message)

        // Assert
        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.SET_LIGHT_REQUEST)
                    assertThat(it.hasSetLightRequest()).isTrue
                    assertThat(it.setLightRequest).isNotNull
                },
            )
        }
    }

    @Test
    fun `should handle get status device request message`() {
        // Arrange
        val message = MockFactory.deviceRequestObjectMessageMock(MockFactory.REQUEST_TYPE_GET_STATUS)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        // Act
        deviceRequestMessageListener.onMessage(message)

        // Assert
        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.GET_STATUS_REQUEST)
                },
            )
        }
    }
}
