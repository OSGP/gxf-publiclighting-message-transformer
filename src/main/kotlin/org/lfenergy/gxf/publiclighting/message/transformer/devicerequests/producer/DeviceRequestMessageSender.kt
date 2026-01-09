// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.BytesMessage
import jakarta.jms.Session
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config.DeviceRequestsConfigurationProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class DeviceRequestMessageSender(
    @param:Qualifier("deviceRequestsJmsTemplate") private val jmsTemplate: JmsTemplate,
    private val properties: DeviceRequestsConfigurationProperties,
) {
    private val logger = KotlinLogging.logger {}

    fun send(message: DeviceRequestMessage) {
        val correlationUid = message.header.correlationUid
        val deviceIdentification = message.header.deviceIdentification
        val messageType = message.header.requestType
        logger.info {
            "Sending device request message with correlation uid $correlationUid for device $deviceIdentification of type $messageType."
        }
        try {
            jmsTemplate.send(properties.producer.outboundQueue) { session -> createBytesMessage(session, message) }
        } catch (e: Exception) {
            logger.error(e) {
                "Failed to send device request message with correlation uid $correlationUid for device $deviceIdentification of type $messageType."
            }
        }
    }

    private fun createBytesMessage(
        session: Session,
        message: DeviceRequestMessage,
    ): BytesMessage {
        val header = message.header
        return session.createBytesMessage().apply {
            jmsType = header.requestType.name
            jmsCorrelationID = header.correlationUid
            jmsPriority = header.priority
            setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, header.deviceIdentification)
            setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, header.organizationIdentification)
            setStringProperty(JMS_PROPERTY_NETWORK_ADDRESS, header.networkAddress)
            setStringProperty(JMS_PROPERTY_DOMAIN, header.domain)
            setStringProperty(JMS_PROPERTY_DOMAIN_VERSION, header.domainVersion)
            writeBytes(message.toByteArray())
        }
    }
}
