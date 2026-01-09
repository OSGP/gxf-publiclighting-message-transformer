// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.consumer

import com.google.protobuf.InvalidProtocolBufferException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.BytesMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.producer.DeviceResponseMessageSender
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class DeviceResponseMessageListener(
    private val deviceResponseMessageSender: DeviceResponseMessageSender,
) {
    private val logger = KotlinLogging.logger { }

    @JmsListener(
        destination = $$"${device-responses.consumer.inbound-queue}",
        containerFactory = "deviceResponsesJmsListenerContainerFactory",
    )
    fun onMessage(bytesMessage: BytesMessage) {
        val correlationId = bytesMessage.jmsCorrelationID
        val deviceId = bytesMessage.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
        val messageType = bytesMessage.jmsType

        logger.info { "Received response for device $deviceId of type $messageType with correlation uid $correlationId." }
        try {
            deviceResponseMessageSender.send(bytesMessage.parse())
        } catch (e: InvalidProtocolBufferException) {
            logger.error(e) { "Received invalid protocol buffer message with correlation uid $correlationId." }
        } catch (e: IllegalArgumentException) {
            logger.error(e) { "Received invalid response for device $deviceId in message with correlation uid $correlationId." }
        }
    }

    private fun BytesMessage.parse(): DeviceResponseMessage {
        val length = this.bodyLength.toInt()
        val bytes = ByteArray(length)
        this.readBytes(bytes)
        return DeviceResponseMessage.parseFrom(bytes)
    }
}
