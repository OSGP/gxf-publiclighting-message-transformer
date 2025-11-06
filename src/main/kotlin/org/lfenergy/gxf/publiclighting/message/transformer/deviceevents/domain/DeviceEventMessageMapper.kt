// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain

import com.google.protobuf.Timestamp
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType.DEVICE_NOTIFICATION
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType.DEVICE_REGISTRATION
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage as ProtobufMessage

object DeviceEventMessageMapper {
    fun ProtobufMessage.toDeviceEventMessageDto(): DeviceEventMessageDto =
        DeviceEventMessageDto(
            deviceIdentification = header.deviceIdentification,
            correlationUid = header.correlationUid,
            organisationIdentification = header.organizationIdentification,
            messageType = header.eventType.name,
            payload =
                when (header.eventType) {
                    DEVICE_REGISTRATION -> toDeviceRegistrationDataDto()
                    DEVICE_NOTIFICATION -> toDeviceNotificationDataDto()
                    else -> throw IllegalArgumentException("Unsupported event type: ${header.eventType}")
                },
        )

    private fun ProtobufMessage.toDeviceRegistrationDataDto() =
        with(deviceRegistrationReceivedEvent) {
            DeviceRegistrationDataDto(
                deviceType = header.deviceType,
                hasSchedule = hasSchedule,
                ipAddress = networkAddress,
            )
        }

    private fun ProtobufMessage.toDeviceNotificationDataDto() =
        with(deviceNotificationReceivedEvent) {
            EventNotificationDto(
                dateTime = timestamp.toZonedDateTime(),
                description = description,
                deviceUid = "",
                eventType = EventTypeDto.valueOf(notificationType.name),
                index = index,
            )
        }

    private fun Timestamp.toZonedDateTime(): ZonedDateTime {
        val instant = Instant.ofEpochSecond(seconds, nanos.toLong())
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
    }
}
