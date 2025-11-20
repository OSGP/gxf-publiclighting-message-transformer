// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config.DeviceRequestsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageDto
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageMapper.toProtobufMessage
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class DeviceRequestMessageSender(
    private val jmsTemplate: JmsTemplate,
    private val properties: DeviceRequestsConfigurationProperties,
) {
    private val logger = KotlinLogging.logger {}

    fun send(messageDto: DeviceRequestMessageDto) {
        logger.info { "Sending device request message for device ${messageDto.deviceIdentification} of type ${messageDto.messageType}." }

        jmsTemplate.send(properties.producer.outboundQueue) { session ->
            val msg = session.createBytesMessage()
            msg.jmsType = messageDto.messageType.name
            msg.jmsCorrelationID = messageDto.correlationUid
            msg.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, messageDto.deviceIdentification)
            msg.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, messageDto.organizationIdentification)
            msg.writeBytes(messageDto.toProtobufMessage().toByteArray())
            msg
        }
    }
}
