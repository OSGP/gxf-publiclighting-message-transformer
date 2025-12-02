// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetScheduleRequest
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.DeviceRequestMessageMapper.toProtobufMessage
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestObjectMessageMockFactory as MockFactory

class DeviceRequestMessageMapperTest {
    @Test
    fun `should map get status request object message to protobuf message`() {
        // Arrange
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.GET_STATUS)

        // Act
        val result = message.toProtobufMessage()

        // Assert
        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(RequestType.GET_STATUS_REQUEST)
    }

    @Test
    fun `should map set light device request object message to protobuf message`() {
        // Arrange
        val message = MockFactory.deviceRequestObjectMessageMock(ObjectMessageType.SET_LIGHT)

        // Act
        val result = message.toProtobufMessage()

        // Assert
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
}
