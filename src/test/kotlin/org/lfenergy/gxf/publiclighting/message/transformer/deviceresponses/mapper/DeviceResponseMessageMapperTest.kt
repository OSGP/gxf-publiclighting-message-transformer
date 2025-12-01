// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
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
    fun `should create set light protocol response message dto from protobuf message`() {
        // Arrange
        val message = DeviceResponseMessageFactory.protobufMessageForResponseOfType(ResponseType.SET_LIGHT_RESPONSE)

        // Act
        val result = message.toResponseDto()

        // Assert
        assertThat(result).isInstanceOf(ProtocolResponseMessage::class.java)
        assertThat(result.result).isEqualTo(ResponseMessageResultType.OK)
        assertThat(result.dataObject).isNull()
    }

    @Test
    fun `should create set schedule protocol response message dto from protobuf message`() {
        // Arrange
        val message = DeviceResponseMessageFactory.protobufMessageForResponseOfType(ResponseType.SET_SCHEDULE_RESPONSE)

        // Act
        val result = message.toResponseDto()

        // Assert
        assertThat(result).isInstanceOf(ProtocolResponseMessage::class.java)
        assertThat(result.result).isEqualTo(ResponseMessageResultType.OK)
        assertThat(result.dataObject).isNull()
    }
}
