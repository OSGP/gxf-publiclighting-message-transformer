// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.mapper

import com.google.protobuf.Timestamp
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
import org.opensmartgridplatform.dto.valueobjects.EventTypeDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage as ProtobufMessage

object DeviceEventMessageMapper {
    fun ProtobufMessage.toRequestMessage() =
        RequestMessage(
            deviceIdentification = header.deviceIdentification,
            organisationIdentification = header.organizationIdentification,
            correlationUid = header.correlationUid,
            request =
                when (header.eventType) {
                    EventType.DEVICE_REGISTRATION -> toDeviceRegistrationDataDto()
                    EventType.DEVICE_REGISTRATION_CONFIRMATION -> null
                    EventType.DEVICE_NOTIFICATION -> toDeviceNotificationDataDto()
                    else -> throw IllegalArgumentException("Unsupported event type: ${header.eventType}")
                },
        )

    fun EventType.toMessageType() =
        when (this) {
            EventType.DEVICE_REGISTRATION -> "REGISTER_DEVICE"
            EventType.DEVICE_REGISTRATION_CONFIRMATION -> "DEVICE_REGISTRATION_COMPLETED"
            EventType.DEVICE_NOTIFICATION -> "EVENT_NOTIFICATION"
            else -> throw IllegalArgumentException("Unsupported event type: $this")
        }

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
