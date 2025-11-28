// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.DeviceEventMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.mapper.DeviceEventMessageMapper.toRequestMessage
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage

class DeviceEventMessageMapperTest {
    @Test
    fun `should create device registration event dto from protobuf message`() {
        // Arrange
        val message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)

        // Act
        val result = message.toRequestMessage()

        // Assert
        assertThat(result).isInstanceOf(RequestMessage::class.java)
        assertThat(result.request).isInstanceOf(DeviceRegistrationDataDto::class.java)
    }

    @Test
    fun `should create device registration confirmation event dto from protobuf message`() {
        // Arrange
        val message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION_CONFIRMATION)

        // Act
        val result = message.toRequestMessage()

        // Assert
        assertThat(result).isInstanceOf(RequestMessage::class.java)
        assertThat(result.request).isNull()
    }

    @Test
    fun `should create device notification event dto from protobuf message`() {
        // Arrange
        val message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_NOTIFICATION)

        // Act
        val result = message.toRequestMessage()

        // Assert
        assertThat(result).isInstanceOf(RequestMessage::class.java)
        assertThat(result.request).isInstanceOf(EventNotificationDto::class.java)
    }

    @Test
    fun `should throw exception for unsupported event type`() {
        // Arrange
        val message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.UNRECOGNIZED)

        // Act
        val result = catchThrowable { message.toRequestMessage() }

        // Assert
        assertThat(result)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Unsupported event type: UNRECOGNIZED")
    }
}
