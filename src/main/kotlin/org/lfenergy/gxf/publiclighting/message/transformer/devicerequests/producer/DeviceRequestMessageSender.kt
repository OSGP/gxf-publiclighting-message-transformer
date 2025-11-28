// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer

import io.github.oshai.kotlinlogging.KotlinLogging
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
        val deviceIdentification = message.header.deviceIdentification
        val messageType = message.header.requestType

        logger.info { "Sending device request message for device $deviceIdentification of type $messageType." }

        jmsTemplate.send(properties.producer.outboundQueue) { session ->
            val msg = session.createBytesMessage()
            msg.jmsType = messageType.name
            msg.jmsCorrelationID = message.header.correlationUid
            msg.jmsPriority = message.header.priority
            msg.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, deviceIdentification)
            msg.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, message.header.organizationIdentification)
            msg.setStringProperty(JMS_PROPERTY_NETWORK_ADDRESS, message.header.networkAddress)
            msg.setStringProperty(JMS_PROPERTY_DOMAIN, message.header.domain)
            msg.setStringProperty(JMS_PROPERTY_DOMAIN_VERSION, message.header.domainVersion)
            msg.writeBytes(message.toByteArray())
            msg
        }
    }
}
