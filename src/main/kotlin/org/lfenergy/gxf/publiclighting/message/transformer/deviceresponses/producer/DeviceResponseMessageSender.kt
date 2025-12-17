// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.ObjectMessage
import jakarta.jms.Session
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config.DeviceResponsesConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.DeviceResponseMessageMapper.toDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.DeviceResponseMessageMapper.toResponseDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class DeviceResponseMessageSender(
    @param:Qualifier("deviceResponsesJmsTemplate") private val jmsTemplate: JmsTemplate,
    private val properties: DeviceResponsesConfigurationProperties,
) {
    private val logger = KotlinLogging.logger {}

    fun send(protobufMessage: DeviceResponseMessage) {
        val header = protobufMessage.header
        val deviceIdentification = header.deviceIdentification
        val messageType = header.responseType.toDto()

        logger.info { "Sending device response message for device $deviceIdentification of type $messageType." }
        try {
            jmsTemplate.send(properties.producer.outboundQueue) { session -> createObjectMessage(session, protobufMessage) }
        } catch (e: Exception) {
            logger.error(e) { "Failed to send device response message for device $deviceIdentification of type $messageType." }
        }
    }

    private fun createObjectMessage(
        session: Session,
        message: DeviceResponseMessage,
    ): ObjectMessage {
        val header = message.header
        return session.createObjectMessage().apply {
            jmsType = header.responseType.toDto()
            jmsCorrelationID = header.correlationUid
            jmsPriority = header.priority
            setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, header.deviceIdentification)
            setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, header.organizationIdentification)
            setStringProperty(JMS_PROPERTY_DOMAIN, header.domain)
            setStringProperty(JMS_PROPERTY_DOMAIN_VERSION, header.domainVersion)
            `object` = message.toResponseDto()
        }
    }
}
