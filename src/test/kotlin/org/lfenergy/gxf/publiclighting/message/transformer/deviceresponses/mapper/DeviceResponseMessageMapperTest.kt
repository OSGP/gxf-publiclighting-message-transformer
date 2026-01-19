// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.DeviceResponseTestHelper.payloadPerResponseTypeAssertions
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponseMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.DeviceResponseMessageMapper.toResponseDto
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType
import java.util.stream.Stream

class DeviceResponseMessageMapperTest {
    @ParameterizedTest(name = "transform {0} to {1}")
    @MethodSource("messageTypesProvider")
    fun `should create protocol response message from protobuf message`(
        inboundResponseType: ResponseType,
        expectedOutboundMessageType: ObjectMessageType,
    ) {
        val message = InboundResponseMessageFactory.protobufMessageForResponseOfType(inboundResponseType)

        val result = message.toResponseDto()

        assertThat(result).isInstanceOf(ProtocolResponseMessage::class.java)
        assertThat(result.result).isEqualTo(ResponseMessageResultType.OK)
        assertThat(result.messageType).isEqualTo(expectedOutboundMessageType.name)

        payloadPerResponseTypeAssertions[inboundResponseType]?.invoke(result)
    }

    companion object {
        @JvmStatic
        private fun messageTypesProvider() =
            Stream.of(
                Arguments.of(ResponseType.GET_CONFIGURATION_RESPONSE, ObjectMessageType.GET_CONFIGURATION),
                Arguments.of(ResponseType.GET_FIRMWARE_VERSION_RESPONSE, ObjectMessageType.GET_FIRMWARE_VERSION),
                Arguments.of(ResponseType.GET_LIGHT_STATUS_RESPONSE, ObjectMessageType.GET_LIGHT_STATUS),
                Arguments.of(ResponseType.GET_STATUS_RESPONSE, ObjectMessageType.GET_STATUS),
                Arguments.of(ResponseType.REBOOT_RESPONSE, ObjectMessageType.SET_REBOOT),
                Arguments.of(ResponseType.RESUME_SCHEDULE_RESPONSE, ObjectMessageType.RESUME_SCHEDULE),
                Arguments.of(ResponseType.SET_CONFIGURATION_RESPONSE, ObjectMessageType.SET_CONFIGURATION),
                Arguments.of(ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE, ObjectMessageType.SET_EVENT_NOTIFICATIONS),
                Arguments.of(ResponseType.SET_LIGHT_RESPONSE, ObjectMessageType.SET_LIGHT),
                Arguments.of(ResponseType.SET_SCHEDULE_RESPONSE, ObjectMessageType.SET_SCHEDULE),
                Arguments.of(ResponseType.SET_TRANSITION_RESPONSE, ObjectMessageType.SET_TRANSITION),
                Arguments.of(ResponseType.START_SELF_TEST_RESPONSE, ObjectMessageType.START_SELF_TEST),
                Arguments.of(ResponseType.STOP_SELF_TEST_RESPONSE, ObjectMessageType.STOP_SELF_TEST),
            )
    }
}
