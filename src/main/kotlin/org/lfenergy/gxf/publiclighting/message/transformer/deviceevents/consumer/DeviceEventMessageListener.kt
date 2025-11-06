// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain.DeviceEventMessageMapper.toDeviceEventMessageDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer.DeviceEventMessageSender
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty("device-events.enabled", havingValue = "true")
class DeviceEventMessageListener(
    val deviceEventMessageSender: DeviceEventMessageSender,
) {
    private val logger = KotlinLogging.logger { }

    @JmsListener(destination = $$"${device-events.consumer.inbound-queue}")
    fun onMessage(event: DeviceEventMessage) {
        logger.info { "Received event for device ${event.header.deviceIdentification} of type ${event.header.eventType}." }
        try {
            deviceEventMessageSender.send(event.toDeviceEventMessageDto())
        } catch (e: IllegalArgumentException) {
            logger.error(e) { "Received invalid event for device ${event.header.deviceIdentification}." }
        }
    }
}
