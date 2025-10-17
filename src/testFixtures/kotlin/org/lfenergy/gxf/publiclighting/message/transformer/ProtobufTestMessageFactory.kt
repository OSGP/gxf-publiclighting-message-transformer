// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceNotificationReceivedEvent
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceRegistrationReceivedEvent
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.Header
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.NotificationType

object ProtobufTestMessageFactory {
    private const val DEVICE_IDENTIFICATION = "device1"
    private const val CORRELATION_UID = "corr1"
    private const val UNRECOGNIZED_VALUE = -1

    fun protobufMessageForEventOfType(eventType: EventType): DeviceEventMessage =
        when (eventType) {
            EventType.DEVICE_REGISTRATION -> deviceRegistrationEventMessage()
            EventType.DEVICE_NOTIFICATION -> deviceNotificationEventMessage()
            else -> unrecognizedEventMessage()
        }

    private fun deviceRegistrationEventMessage() =
        DeviceEventMessage
            .newBuilder()
            .setHeader(
                Header
                    .newBuilder()
                    .setDeviceIdentification(DEVICE_IDENTIFICATION)
                    .setCorrelationUid(CORRELATION_UID)
                    .setEventType(EventType.DEVICE_REGISTRATION)
                    .build(),
            ).setDeviceRegistrationReceivedEvent(
                DeviceRegistrationReceivedEvent
                    .newBuilder()
                    .setNetworkAddress("127.0.0.1")
                    .setHasSchedule(true)
                    .build(),
            ).build()

    private fun deviceNotificationEventMessage() =
        DeviceEventMessage
            .newBuilder()
            .setHeader(
                Header
                    .newBuilder()
                    .setDeviceIdentification(DEVICE_IDENTIFICATION)
                    .setCorrelationUid(CORRELATION_UID)
                    .setEventType(EventType.DEVICE_NOTIFICATION)
                    .build(),
            ).setDeviceNotificationReceivedEvent(
                DeviceNotificationReceivedEvent
                    .newBuilder()
                    .setNotificationType(NotificationType.LIGHT_EVENTS_LIGHT_ON)
                    .setDescription("Light turned on")
                    .setIndex(0)
                    .build(),
            ).build()

    private fun unrecognizedEventMessage() =
        DeviceEventMessage
            .newBuilder()
            .setHeader(
                Header
                    .newBuilder()
                    .setDeviceIdentification(DEVICE_IDENTIFICATION)
                    .setCorrelationUid(CORRELATION_UID)
                    .setEventTypeValue(UNRECOGNIZED_VALUE)
                    .build(),
            ).build()
}
