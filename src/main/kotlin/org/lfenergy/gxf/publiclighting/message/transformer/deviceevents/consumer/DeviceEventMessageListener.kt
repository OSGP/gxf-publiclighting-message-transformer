// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.consumer

import com.google.protobuf.InvalidProtocolBufferException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.BytesMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageMapper.mapToDeviceEventMessageDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer.DeviceEventMessageSender
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty("device-events.enabled", havingValue = "true")
class DeviceEventMessageListener(
    private val deviceEventMessageSender: DeviceEventMessageSender,
) {
    private val logger = KotlinLogging.logger { }

    @JmsListener(destination = $$"${device-events.consumer.inbound-queue}")
    fun onMessage(bytesMessage: BytesMessage) {
        val correlationId = bytesMessage.jmsCorrelationID
        val deviceId = bytesMessage.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
        val messageType = bytesMessage.jmsType

        logger.info {
            "Received event for device $deviceId of type $messageType with correlation uid $correlationId."
        }

        try {
            val deviceEventMessage = bytesMessage.parseIntoDeviceEventMessage()
            val deviceEventMessageDto = deviceEventMessage.mapToDeviceEventMessageDto()
            deviceEventMessageSender.send(deviceEventMessageDto)
        } catch (e: InvalidProtocolBufferException) {
            logger.error {
                "Received invalid protocol buffer message with correlation uid $correlationId."
            }
        } catch (e: IllegalArgumentException) {
            logger.error(e) {
                "Received invalid event for device $deviceId in message with correlation uid $correlationId."
            }
        }
    }

    private fun BytesMessage.parseIntoDeviceEventMessage(): DeviceEventMessage {
        val length = this.bodyLength.toInt()
        val bytes = ByteArray(length)
        this.readBytes(bytes)
        return DeviceEventMessage.parseFrom(bytes)
    }
}
