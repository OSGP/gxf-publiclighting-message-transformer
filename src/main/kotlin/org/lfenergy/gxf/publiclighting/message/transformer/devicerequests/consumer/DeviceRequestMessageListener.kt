// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.consumer

import com.google.protobuf.InvalidProtocolBufferException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageDto
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageType.SET_LIGHT_REQUEST
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageType.SET_SCHEDULE_REQUEST
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer.DeviceRequestMessageSender
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty("device-requests.enabled", havingValue = "true")
class DeviceRequestMessageListener(
    private val deviceRequestMessageSender: DeviceRequestMessageSender,
) {
    private val logger = KotlinLogging.logger { }

    @JmsListener(destination = $$"${device-events.consumer.inbound-queue}")
    fun onMessage(objectMessage: ObjectMessage) {
        val correlationId = objectMessage.jmsCorrelationID
        val deviceId = objectMessage.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
        val messageType = objectMessage.jmsType

        logger.info {
            "Received request for device $deviceId of type $messageType with correlation uid $correlationId."
        }

        try {
            val deviceRequestMessageDto = objectMessage.mapToDeviceRequestMessageDto()
            deviceRequestMessageSender.send(deviceRequestMessageDto)
        } catch (e: InvalidProtocolBufferException) {
            logger.error {
                "Received invalid object message with correlation uid $correlationId."
            }
        } catch (e: IllegalArgumentException) {
            logger.error(e) {
                "Received invalid request for device $deviceId in message with correlation uid $correlationId."
            }
        }
    }

    private fun ObjectMessage.mapToDeviceRequestMessageDto() =
        DeviceRequestMessageDto(
            deviceIdentification = this.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION),
            correlationUid = this.jmsCorrelationID,
            organizationIdentification = this.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION),
            messageType = this.jmsType.toDeviceRequestMessageType(),
            payload = this.`object`,
        )

    private fun String.toDeviceRequestMessageType() =
        when (this) {
            "SET_LIGHT" -> SET_LIGHT_REQUEST
            "SET_SCHEDULE" -> SET_SCHEDULE_REQUEST
            else -> throw IllegalArgumentException("Unsupported message type: $this")
        }
}
