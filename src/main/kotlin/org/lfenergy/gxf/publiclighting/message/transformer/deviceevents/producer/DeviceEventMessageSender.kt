// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.ObjectMessage
import jakarta.jms.Session
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
        val header = protobufMessage.header
        val correlationUid = header.correlationUid
        val deviceIdentification = header.deviceIdentification
        val messageType = header.eventType

        logger.info {
            "Sending device event message with correlationUid $correlationUid for device $deviceIdentification of type $messageType."
        }
        try {
            jmsTemplate.send(properties.producer.outboundQueue) { session -> createObjectMessage(session, protobufMessage) }
        } catch (e: Exception) {
            logger.error(e) {
                "Failed to send device event message with correlationUid $correlationUid for device $deviceIdentification of type $messageType."
            }
        }
    }

    private fun createObjectMessage(
        session: Session,
        message: DeviceEventMessage,
    ): ObjectMessage {
        val header = message.header
        return session.createObjectMessage().apply {
            jmsType = header.eventType.toMessageType()
            jmsCorrelationID = header.correlationUid
            setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, header.deviceIdentification)
            setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, header.organizationIdentification)
            `object` = message.toRequestMessage()
        }
    }
}
