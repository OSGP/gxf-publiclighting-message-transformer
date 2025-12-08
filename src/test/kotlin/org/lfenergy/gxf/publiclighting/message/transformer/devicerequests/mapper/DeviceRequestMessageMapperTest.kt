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
    fun `should map get status request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.GET_STATUS)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.GET_STATUS_REQUEST)
        verifyNoPayload(result)
    }

    @Test
    fun `should map reboot device request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_REBOOT)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.REBOOT_REQUEST)
        verifyNoPayload(result)
    }

    @Test
    fun `should map resume schedule device request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.RESUME_SCHEDULE)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.RESUME_SCHEDULE_REQUEST)
        assertThat(result.hasResumeScheduleRequest()).isTrue
        assertThat(result.resumeScheduleRequest).isInstanceOf(ResumeScheduleRequest::class.java)
    }

    @Test
    fun `should map set light device request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_LIGHT)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.SET_LIGHT_REQUEST)
        assertThat(result.hasSetLightRequest()).isTrue
        assertThat(result.setLightRequest).isInstanceOf(SetLightRequest::class.java)
    }

    @Test
    fun `should map set schedule device request object message to protobuf message`() {
        // Arrange
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_SCHEDULE)

        // Act
        val result = message.toProtobufMessage()

        // Assert
        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.SET_SCHEDULE_REQUEST)
        assertThat(result.hasSetScheduleRequest()).isTrue
        assertThat(result.setScheduleRequest).isInstanceOf(SetScheduleRequest::class.java)
    }

    @Test
    fun `should map set transition device request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_TRANSITION)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.SET_TRANSITION_REQUEST)
        assertThat(result.hasSetTransitionRequest()).isTrue
        assertThat(result.setTransitionRequest).isNotNull
    }

    @Test
    fun `should map start self test device request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.START_SELF_TEST)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.START_SELF_TEST_REQUEST)
        verifyNoPayload(result)
    }

    @Test
    fun `should map stop self test device request object message to protobuf message`() {
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.STOP_SELF_TEST)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.STOP_SELF_TEST_REQUEST)
        verifyNoPayload(result)
    }

    private fun verifyNoPayload(deviceRequestMessage: DeviceRequestMessage) {
        assertThat(deviceRequestMessage.hasResumeScheduleRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetScheduleRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetLightRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetTransitionRequest()).isFalse
    }
}
