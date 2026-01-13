// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.ObjectMessage
import jakarta.jms.Session
import org.lfenergy.gxf.publiclighting.contracts.internal.audittrail.LogItemMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DECODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ENCODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_IS_INCOMING
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.config.DeviceMessageLogsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper.LogItemMessageMapper.toDecodedMessage
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper.LogItemMessageMapper.toIsIncoming
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class LogItemMessageSender(
    @param:Qualifier("deviceMessageLogsJmsTemplate") private val jmsTemplate: JmsTemplate,
    private val properties: DeviceMessageLogsConfigurationProperties,
) {
    private val logger = KotlinLogging.logger { }

    fun send(protobufMessage: LogItemMessage) {
        val deviceIdentification = protobufMessage.deviceIdentification
        val messageType = protobufMessage.messageType

        logger.info { "Sending device message log item for device $deviceIdentification." }
        try {
            jmsTemplate.send(properties.producer.outboundQueue) { session ->
                createObjectMessage(session, protobufMessage)
            }
        } catch (e: Exception) {
            logger.error { "Failed to send device message log item for device $deviceIdentification." }
        }
    }

    private fun createObjectMessage(
        session: Session,
        message: LogItemMessage,
    ): ObjectMessage =
        session.createObjectMessage().apply {
            setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, message.deviceIdentification)
            setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, message.organizationIdentification)
            setStringProperty(JMS_PROPERTY_DECODED_MESSAGE, message.rawData.toDecodedMessage())
            setStringProperty(JMS_PROPERTY_ENCODED_MESSAGE, message.rawData.toStringUtf8())
            setStringProperty(JMS_PROPERTY_IS_INCOMING, message.messageType.toIsIncoming())
            setIntProperty(ApplicationConstants.JMS_PROPERTY_PAYLOAD_MESSAGE_SERIALIZED_SIZE, message.rawDataSize)
        }
}
