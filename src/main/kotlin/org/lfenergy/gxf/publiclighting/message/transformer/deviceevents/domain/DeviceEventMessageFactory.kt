// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain

import com.google.protobuf.Timestamp
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage as ProtobufMessage

@Component
class DeviceEventMessageFactory {
    fun fromProtobufMessage(message: ProtobufMessage) =
        when (message.header.eventType) {
            EventType.DEVICE_REGISTRATION -> deviceRegistrationReceived(message)
            EventType.DEVICE_NOTIFICATION -> deviceNotificationReceived(message)
            else -> throw IllegalArgumentException("Unsupported event type: ${message.header.eventType}")
        }

    private fun deviceRegistrationReceived(message: ProtobufMessage) =
        DeviceEventMessageDto(
            deviceIdentification = message.header.deviceIdentification,
            correlationUid = message.header.correlationUid,
            organisationIdentification = message.header.organizationIdentification,
            messageType = message.header.eventType.name,
            payload = toDeviceRegistrationDataDto(message),
        )

    private fun deviceNotificationReceived(message: ProtobufMessage) =
        DeviceEventMessageDto(
            deviceIdentification = message.header.deviceIdentification,
            correlationUid = message.header.correlationUid,
            organisationIdentification = message.header.organizationIdentification,
            messageType = message.header.eventType.name,
            payload = toDeviceNotificationDataDto(message),
        )

    private fun toDeviceRegistrationDataDto(message: ProtobufMessage) =
        with(message.deviceRegistrationReceivedEvent) {
            DeviceRegistrationDataDto(
                ipAddress = networkAddress,
                deviceType = message.header.deviceType,
                hasSchedule = hasSchedule,
            )
        }

    private fun toDeviceNotificationDataDto(message: ProtobufMessage) =
        with(message.deviceNotificationReceivedEvent) {
            EventNotificationDto(
                description = description,
                index = index,
                deviceUid = "", // deviceUid,
                dateTime = toZonedDateTime(timestamp),
                eventType = EventTypeDto.valueOf(notificationType.name),
            )
        }

    private fun toZonedDateTime(timestamp: Timestamp): ZonedDateTime {
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong())
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
    }
}
