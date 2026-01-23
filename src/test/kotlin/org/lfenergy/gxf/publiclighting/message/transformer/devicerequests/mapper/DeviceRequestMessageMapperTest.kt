// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestTestHelper.payloadPerRequestTypeAssertions
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.DeviceRequestMessageMapper.toProtobufMessage
import java.util.stream.Stream
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestObjectMessageMockFactory as MockFactory

class DeviceRequestMessageMapperTest {
    @ParameterizedTest(name = "transform {0} to {1}")
    @MethodSource("messageTypesProvider")
    fun `should create proto message from request object message`(
        inboundMessageType: ObjectMessageType,
        expectedOutboundRequestType: RequestType,
    ) {
        val message = MockFactory.deviceRequestObjectMessageMock(inboundMessageType)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(expectedOutboundRequestType)
        assertThat(result.header.domain).isEqualTo(TestConstants.DOMAIN)
        assertThat(result.header.domainVersion).isEqualTo(TestConstants.DOMAIN_VERSION)
        payloadPerRequestTypeAssertions[inboundMessageType]?.invoke(result)
    }

    companion object {
        @JvmStatic
        private fun messageTypesProvider() =
            Stream.of(
                Arguments.of(ObjectMessageType.GET_CONFIGURATION, RequestType.GET_CONFIGURATION_REQUEST),
                Arguments.of(ObjectMessageType.GET_FIRMWARE_VERSION, RequestType.GET_FIRMWARE_VERSION_REQUEST),
                Arguments.of(ObjectMessageType.GET_LIGHT_STATUS, RequestType.GET_LIGHT_STATUS_REQUEST),
                Arguments.of(ObjectMessageType.GET_STATUS, RequestType.GET_STATUS_REQUEST),
                Arguments.of(ObjectMessageType.RESUME_SCHEDULE, RequestType.RESUME_SCHEDULE_REQUEST),
                Arguments.of(ObjectMessageType.SET_CONFIGURATION, RequestType.SET_CONFIGURATION_REQUEST),
                Arguments.of(ObjectMessageType.SET_EVENT_NOTIFICATIONS, RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST),
                Arguments.of(ObjectMessageType.SET_LIGHT, RequestType.SET_LIGHT_REQUEST),
                Arguments.of(ObjectMessageType.SET_REBOOT, RequestType.REBOOT_REQUEST),
                Arguments.of(ObjectMessageType.SET_LIGHT_SCHEDULE, RequestType.SET_SCHEDULE_REQUEST),
                Arguments.of(ObjectMessageType.SET_TRANSITION, RequestType.SET_TRANSITION_REQUEST),
                Arguments.of(ObjectMessageType.START_SELF_TEST, RequestType.START_SELF_TEST_REQUEST),
                Arguments.of(ObjectMessageType.STOP_SELF_TEST, RequestType.STOP_SELF_TEST_REQUEST),
            )
    }
}
