// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.ProtobufTestMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageMapper.toDeviceEventMessageDto
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto

class DeviceEventMessageMapperTest {
    @Test
    fun `should create device registration message from protobuf message`() {
        // Arrange
        val message = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)

        // Act
        val result = message.toDeviceEventMessageDto()

        // Assert
        assertThat(result).isInstanceOf(DeviceEventMessageDto::class.java)
        assertThat(result.payload).isInstanceOf(DeviceRegistrationDataDto::class.java)
    }

    @Test
    fun `should create device notification message from protobuf message`() {
        // Arrange
        val message = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_NOTIFICATION)

        // Act
        val result = message.toDeviceEventMessageDto()

        // Assert
        assertThat(result).isInstanceOf(DeviceEventMessageDto::class.java)
        assertThat(result.payload).isInstanceOf(EventNotificationDto::class.java)
    }

    @Test
    fun `should throw exception for unsupported event type`() {
        // Arrange
        val message = ProtobufTestMessageFactory.protobufMessageForEventOfType(EventType.UNRECOGNIZED)

        // Act
        val result = catchThrowable { message.toDeviceEventMessageDto() }

        // Assert
        assertThat(result)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Unsupported event type: UNRECOGNIZED")
    }
}
