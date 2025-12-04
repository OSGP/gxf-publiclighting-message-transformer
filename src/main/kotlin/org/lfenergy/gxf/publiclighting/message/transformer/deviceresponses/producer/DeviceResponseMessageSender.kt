// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.producer

import io.github.oshai.kotlinlogging.KotlinLogging
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
        val deviceIdentification = protobufMessage.header.deviceIdentification
        val messageType = protobufMessage.header.responseType.toDto()
        logger.info { "Sending device response message for device $deviceIdentification of type $messageType." }
        try {
            jmsTemplate.send(properties.producer.outboundQueue) { session ->
                val message = session.createObjectMessage()
                message.jmsType = messageType
                message.jmsCorrelationID = protobufMessage.header.correlationUid
                message.jmsPriority = protobufMessage.header.priority
                message.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, protobufMessage.header.deviceIdentification)
                message.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, protobufMessage.header.organizationIdentification)
                message.setStringProperty(JMS_PROPERTY_DOMAIN, protobufMessage.header.domain)
                message.setStringProperty(JMS_PROPERTY_DOMAIN_VERSION, protobufMessage.header.domainVersion)
                message.`object` = protobufMessage.toResponseDto()
                message
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to send device response message for device $deviceIdentification of type $messageType." }
        }
    }
}
