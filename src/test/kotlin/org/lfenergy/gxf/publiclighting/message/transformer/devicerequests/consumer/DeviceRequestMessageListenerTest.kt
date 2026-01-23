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
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer.DeviceRequestMessageSender
import org.springframework.boot.test.system.OutputCaptureExtension
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestObjectMessageMockFactory as MockFactory

@ExtendWith(MockKExtension::class, OutputCaptureExtension::class)
class DeviceRequestMessageListenerTest {
    @MockK
    lateinit var deviceRequestMessageSender: DeviceRequestMessageSender

    @InjectMockKs
    lateinit var deviceRequestMessageListener: DeviceRequestMessageListener

    @Test
    fun `should handle get configuration device request message`() =
        testEmptyRequest(ObjectMessageType.GET_CONFIGURATION, RequestType.GET_CONFIGURATION_REQUEST)

    @Test
    fun `should handle get firmware version device request message`() =
        testEmptyRequest(ObjectMessageType.GET_FIRMWARE_VERSION, RequestType.GET_FIRMWARE_VERSION_REQUEST)

    @Test
    fun `should handle get status device request message`() = testEmptyRequest(ObjectMessageType.GET_STATUS, RequestType.GET_STATUS_REQUEST)

    @Test
    fun `should handle resume schedule device request message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.RESUME_SCHEDULE)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.RESUME_SCHEDULE_REQUEST)
                    assertThat(it.hasResumeScheduleRequest()).isTrue
                    assertThat(it.resumeScheduleRequest).isNotNull
                },
            )
        }
    }

    @Test
    fun `should handle set configuration device request message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_CONFIGURATION)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.SET_CONFIGURATION_REQUEST)
                    assertThat(it.hasSetConfigurationRequest()).isTrue
                    assertThat(it.setConfigurationRequest).isNotNull
                },
            )
        }
    }

    @Test
    fun `should handle set event notification mask device request message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_EVENT_NOTIFICATIONS)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST)
                    assertThat(it.hasSetEventNotificationMaskRequest()).isTrue
                    assertThat(it.setEventNotificationMaskRequest).isNotNull
                },
            )
        }
    }

    @Test
    fun `should handle set light device request message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_LIGHT)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

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
    fun `should handle set reboot device request message`() = testEmptyRequest(ObjectMessageType.SET_REBOOT, RequestType.REBOOT_REQUEST)

    @Test
    fun `should handle set schedule device request message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_LIGHT_SCHEDULE)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.SET_SCHEDULE_REQUEST)
                    assertThat(it.hasSetScheduleRequest()).isTrue
                    assertThat(it.setScheduleRequest).isNotNull
                },
            )
        }
    }

    @Test
    fun `should handle set transition device request message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_TRANSITION)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(RequestType.SET_TRANSITION_REQUEST)
                    assertThat(it.hasSetTransitionRequest()).isTrue
                    assertThat(it.setTransitionRequest).isNotNull
                },
            )
        }
    }

    @Test
    fun `should handle start self test device request message`() =
        testEmptyRequest(ObjectMessageType.START_SELF_TEST, RequestType.START_SELF_TEST_REQUEST)

    @Test
    fun `should handle stop self test device request message`() =
        testEmptyRequest(ObjectMessageType.STOP_SELF_TEST, RequestType.STOP_SELF_TEST_REQUEST)

    private fun testEmptyRequest(
        inboundRequestType: ObjectMessageType,
        expectedOutboundRequestType: RequestType,
    ) {
        val message = MockFactory.deviceRequestObjectMessageMock(inboundRequestType)
        every { deviceRequestMessageSender.send(any<DeviceRequestMessage>()) } just Runs

        deviceRequestMessageListener.onMessage(message)

        verify {
            deviceRequestMessageSender.send(
                withArg {
                    assertThat(it).isInstanceOf(DeviceRequestMessage::class.java)
                    assertThat(it.header.requestType).isEqualTo(expectedOutboundRequestType)
                },
            )
        }
    }
}
