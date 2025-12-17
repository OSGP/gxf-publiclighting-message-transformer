// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponseMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.DeviceResponseMessageMapper.toResponseDto
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType

class DeviceResponseMessageMapperTest {
    @Test
    fun `should create get configuration protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.GET_CONFIGURATION_RESPONSE, ObjectMessageType.GET_CONFIGURATION)

    @Test
    fun `should create get firmware version protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.GET_FIRMWARE_VERSION_RESPONSE, ObjectMessageType.GET_FIRMWARE_VERSION)

    @Test
    fun `should create get status protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.GET_STATUS_RESPONSE, ObjectMessageType.GET_STATUS)

    @Test
    fun `should create resume schedule protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.RESUME_SCHEDULE_RESPONSE, ObjectMessageType.RESUME_SCHEDULE)

    @Test
    fun `should create set configuration protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.SET_CONFIGURATION_RESPONSE, ObjectMessageType.SET_CONFIGURATION)

    @Test
    fun `should create set event notification mask protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(
            ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE,
            ObjectMessageType.SET_EVENT_NOTIFICATIONS,
        )

    @Test
    fun `should create set light protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.SET_LIGHT_RESPONSE, ObjectMessageType.SET_LIGHT)

    @Test
    fun `should create set reboot protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.REBOOT_RESPONSE, ObjectMessageType.SET_REBOOT)

    @Test
    fun `should create set schedule protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.SET_SCHEDULE_RESPONSE, ObjectMessageType.SET_SCHEDULE)

    @Test
    fun `should create set transition protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.SET_TRANSITION_RESPONSE, ObjectMessageType.SET_TRANSITION)

    @Test
    fun `should create start self test protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.START_SELF_TEST_RESPONSE, ObjectMessageType.START_SELF_TEST)

    @Test
    fun `should create stop self test protocol response message dto from protobuf message`() =
        verifyResponseMessageCreated(ResponseType.STOP_SELF_TEST_RESPONSE, ObjectMessageType.STOP_SELF_TEST)

    private fun verifyResponseMessageCreated(
        inboundResponseType: ResponseType,
        outboundMessageType: ObjectMessageType,
    ) {
        val message = InboundResponseMessageFactory.protobufMessageForResponseOfType(inboundResponseType)

        val result = message.toResponseDto()

        assertThat(result).isInstanceOf(ProtocolResponseMessage::class.java)
        assertThat(result.result).isEqualTo(ResponseMessageResultType.OK)
        assertThat(result.messageType).isEqualTo(outboundMessageType.name)

        when (inboundResponseType) {
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> assertThat(result.dataObject).isNotNull.isInstanceOf(List::class.java)
            ResponseType.GET_STATUS_RESPONSE -> assertThat(result.dataObject).isNotNull.isInstanceOf(DeviceStatusDto::class.java)
            ResponseType.GET_CONFIGURATION_RESPONSE -> assertThat(result.dataObject).isNotNull.isInstanceOf(ConfigurationDto::class.java)
            else -> assertThat(result.dataObject).isNull()
        }
    }
}
