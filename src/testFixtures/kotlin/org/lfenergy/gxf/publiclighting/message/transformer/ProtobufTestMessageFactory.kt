// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.NotificationType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.deviceEventMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.deviceNotificationReceivedEvent
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.deviceRegistrationReceivedEvent
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.header

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
        deviceEventMessage {
            header = header {
                deviceIdentification = DEVICE_IDENTIFICATION
                correlationUid = CORRELATION_UID
                eventType = EventType.DEVICE_REGISTRATION
            }
            deviceRegistrationReceivedEvent = deviceRegistrationReceivedEvent {
                networkAddress = "127.0.0.1"
                hasSchedule = true
            }
        }

    private fun deviceNotificationEventMessage() =
        deviceEventMessage {
            header = header {
                deviceIdentification = DEVICE_IDENTIFICATION
                correlationUid = CORRELATION_UID
                eventType = EventType.DEVICE_NOTIFICATION
            }
            deviceNotificationReceivedEvent = deviceNotificationReceivedEvent {
                notificationType = NotificationType.LIGHT_EVENTS_LIGHT_ON
                description = "Light turned on"
                index = 0
            }
        }

    private fun unrecognizedEventMessage() =
        deviceEventMessage {
            header = header {
                deviceIdentification = DEVICE_IDENTIFICATION
                correlationUid = CORRELATION_UID
                eventTypeValue = UNRECOGNIZED_VALUE
            }
        }
}
