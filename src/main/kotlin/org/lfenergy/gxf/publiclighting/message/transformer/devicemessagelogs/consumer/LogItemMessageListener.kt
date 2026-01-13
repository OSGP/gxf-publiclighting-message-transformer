// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.consumer

import com.google.protobuf.InvalidProtocolBufferException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.BytesMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.audittrail.LogItemMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.producer.LogItemMessageSender
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class LogItemMessageListener(
    private val logItemMessageSender: LogItemMessageSender,
) {
    private val logger = KotlinLogging.logger { }

    @JmsListener(
        destination = $$"${device-message-logs.consumer.inbound-queue}",
        containerFactory = "deviceMessageLogsJmsListenerContainerFactory",
    )
    fun onMessage(bytesMessage: BytesMessage) {
        val deviceId = bytesMessage.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
        val messageType = bytesMessage.jmsType

        logger.info { "Received device message log item for device $deviceId." }
        try {
            logItemMessageSender.send(bytesMessage.parse())
        } catch (e: InvalidProtocolBufferException) {
            logger.error(e) { "Received invalid protocol buffer message for device $deviceId." }
        } catch (e: IllegalArgumentException) {
            logger.error(e) { "Received invalid log item for device $deviceId." }
        }
    }

    private fun BytesMessage.parse(): LogItemMessage {
        val length = this.bodyLength.toInt()
        val bytes = ByteArray(length)
        this.readBytes(bytes)
        return LogItemMessage.parseFrom(bytes)
    }
}
