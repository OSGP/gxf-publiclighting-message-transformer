// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ResumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetScheduleRequest
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.DeviceRequestMessageMapper.toProtobufMessage
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestObjectMessageMockFactory as MockFactory

class DeviceRequestMessageMapperTest {
    @Test
    fun `should map get configuration request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.GET_CONFIGURATION, RequestType.GET_CONFIGURATION_REQUEST)

    @Test
    fun `should map get firmware version request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.GET_FIRMWARE_VERSION, RequestType.GET_FIRMWARE_VERSION_REQUEST)

    @Test
    fun `should map get status request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.GET_STATUS, RequestType.GET_STATUS_REQUEST)

    @Test
    fun `should map reboot device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_REBOOT, RequestType.REBOOT_REQUEST)

    @Test
    fun `should map resume schedule device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.RESUME_SCHEDULE, RequestType.RESUME_SCHEDULE_REQUEST)

    fun verifyResumeSchedulePayload(message: DeviceRequestMessage) {
        assertThat(message.hasResumeScheduleRequest()).isTrue
        assertThat(message.resumeScheduleRequest).isInstanceOf(ResumeScheduleRequest::class.java)
    }

    @Test
    fun `should map set event notification mask device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_EVENT_NOTIFICATIONS, RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST)

    fun verifySetEventNotificationPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetEventNotificationMaskRequest()).isTrue
        assertThat(message.setEventNotificationMaskRequest).isNotNull
    }

    @Test
    fun `should map set light device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_LIGHT, RequestType.SET_LIGHT_REQUEST)

    fun verifySetLightPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetLightRequest()).isTrue
        assertThat(message.setLightRequest).isInstanceOf(SetLightRequest::class.java)
    }

    @Test
    fun `should map set schedule device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_SCHEDULE, RequestType.SET_SCHEDULE_REQUEST)

    fun verifySetSchedulePayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetScheduleRequest()).isTrue
        assertThat(message.setScheduleRequest).isInstanceOf(SetScheduleRequest::class.java)
    }

    @Test
    fun `should map set transition device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_TRANSITION, RequestType.SET_TRANSITION_REQUEST)

    fun verifySetTransitionPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetTransitionRequest()).isTrue
        assertThat(message.setTransitionRequest).isNotNull
    }

    @Test
    fun `should map start self test device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.START_SELF_TEST, RequestType.START_SELF_TEST_REQUEST)

    @Test
    fun `should map stop self test device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.STOP_SELF_TEST, RequestType.STOP_SELF_TEST_REQUEST)

    private fun testDeviceRequestMapping(
        inboundMessageType: ObjectMessageType,
        expectedRequestType: RequestType,
    ) {
        val message = MockFactory.deviceRequestObjectMessageMock(inboundMessageType)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(expectedRequestType)

        when (inboundMessageType) {
            ObjectMessageType.RESUME_SCHEDULE -> verifyResumeSchedulePayload(result)
            ObjectMessageType.SET_LIGHT -> verifySetLightPayload(result)
            ObjectMessageType.SET_SCHEDULE -> verifySetSchedulePayload(result)
            ObjectMessageType.SET_TRANSITION -> verifySetTransitionPayload(result)
            ObjectMessageType.SET_EVENT_NOTIFICATIONS -> verifySetEventNotificationPayload(result)
            else -> verifyNoPayload(result)
        }
    }

    private fun verifyNoPayload(deviceRequestMessage: DeviceRequestMessage) {
        assertThat(deviceRequestMessage.hasResumeScheduleRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetScheduleRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetLightRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetTransitionRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetEventNotificationMaskRequest()).isFalse
    }
}
