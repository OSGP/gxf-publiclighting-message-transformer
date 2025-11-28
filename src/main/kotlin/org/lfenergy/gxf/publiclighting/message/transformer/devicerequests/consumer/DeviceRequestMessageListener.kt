// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.DeviceRequestMessageMapper.toProtobufMessage
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

    @JmsListener(
        destination = $$"${device-requests.consumer.inbound-queue}",
        containerFactory = "deviceRequestsJmsListenerContainerFactory",
    )
    fun onMessage(message: ObjectMessage) {
        val correlationId = message.jmsCorrelationID
        val deviceId = message.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
        val messageType = message.jmsType

        logger.info { "Received request for device $deviceId of type $messageType with correlation uid $correlationId." }
        try {
            deviceRequestMessageSender.send(message.toProtobufMessage())
        } catch (e: IllegalArgumentException) {
            logger.error(e) { "Received invalid request for device $deviceId in message with correlation uid $correlationId." }
        }
    }
}
