// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.mapper.DeviceEventMessageMapper.toMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.mapper.DeviceEventMessageMapper.toRequestMessage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class DeviceEventMessageSender(
    @param:Qualifier("deviceEventsJmsTemplate") private val jmsTemplate: JmsTemplate,
    private val properties: DeviceEventsConfigurationProperties,
) {
    private val logger = KotlinLogging.logger {}

    fun send(protobufMessage: DeviceEventMessage) {
        val deviceIdentification = protobufMessage.header.deviceIdentification
        val messageType = protobufMessage.header.eventType
        logger.info { "Sending device event message for device $deviceIdentification of type $messageType." }
        try {
            jmsTemplate.send(properties.producer.outboundQueue) { session ->
                val message = session.createObjectMessage()
                message.jmsType = messageType.toMessageType()
                message.jmsCorrelationID = protobufMessage.header.correlationUid
                message.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, deviceIdentification)
                message.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, protobufMessage.header.organizationIdentification)
                message.`object` = protobufMessage.toRequestMessage()
                message
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to send device event message for device $deviceIdentification of type $messageType." }
        }
    }
}
