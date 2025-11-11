// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.jms.Message
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageDto
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessagePostProcessor
import org.springframework.stereotype.Component

@Component
class DeviceEventMessageSender(
    private val jmsTemplate: JmsTemplate,
    private val properties: DeviceEventsConfigurationProperties,
) {
    private val logger = KotlinLogging.logger {}

    fun send(message: DeviceEventMessageDto) {
        logger.info { "Sending device event message for device ${message.deviceIdentification} of type ${message.messageType}." }
        jmsTemplate.convertAndSend(
            properties.producer.outboundQueue,
            message.payload,
            MessagePostProcessor { msg: Message ->
                msg.jmsType = message.messageType

                msg
            },
        )
    }
}
