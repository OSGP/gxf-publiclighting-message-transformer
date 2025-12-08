// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.DeviceResponseMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.DeviceResponseMessageMapper.toResponseDto
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType

class DeviceResponseMessageMapperTest {
    @Test
    fun `should create get status protocol response message dto from protobuf message`() {
        // Arrange
        val message = DeviceResponseMessageFactory.protobufMessageForResponseOfType(ResponseType.GET_STATUS_RESPONSE)

        // Act
        val result = message.toResponseDto()

        // Assert
        assertThat(result).isInstanceOf(ProtocolResponseMessage::class.java)
        assertThat(result.result).isEqualTo(ResponseMessageResultType.OK)
        assertThat(result.dataObject).isNotNull().isInstanceOf(DeviceStatusDto::class.java)
    }

    @Test
    fun `should create resume schedule protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.RESUME_SCHEDULE_RESPONSE, ObjectMessageType.RESUME_SCHEDULE)

    @Test
    fun `should create set light protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.SET_LIGHT_RESPONSE, ObjectMessageType.SET_LIGHT)

    @Test
    fun `should create set reboot protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.REBOOT_RESPONSE, ObjectMessageType.SET_REBOOT)

    @Test
    fun `should create set schedule protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.SET_SCHEDULE_RESPONSE, ObjectMessageType.SET_SCHEDULE)

    @Test
    fun `should create set transition protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.SET_TRANSITION_RESPONSE, ObjectMessageType.SET_TRANSITION)

    @Test
    fun `should create start self test protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.START_SELF_TEST_RESPONSE, ObjectMessageType.START_SELF_TEST)

    @Test
    fun `should create stop self test protocol response message dto from protobuf message`() =
        verifyEmptyResponseMessageCreated(ResponseType.STOP_SELF_TEST_RESPONSE, ObjectMessageType.STOP_SELF_TEST)

    private fun verifyEmptyResponseMessageCreated(
        inboundResponseType: ResponseType,
        outboundMessageType: ObjectMessageType,
    ) {
        val message = DeviceResponseMessageFactory.protobufMessageForResponseOfType(inboundResponseType)

        val result = message.toResponseDto()

        assertThat(result).isInstanceOf(ProtocolResponseMessage::class.java)
        assertThat(result.result).isEqualTo(ResponseMessageResultType.OK)
        assertThat(result.messageType).isEqualTo(outboundMessageType.name)
        assertThat(result.dataObject).isNull()
    }
}
