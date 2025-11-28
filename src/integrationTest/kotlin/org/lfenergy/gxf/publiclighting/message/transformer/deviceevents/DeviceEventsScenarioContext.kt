// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents

import io.cucumber.spring.ScenarioScope
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.opensmartgridplatform.shared.infra.jms.RequestMessage
import org.springframework.stereotype.Component

@ScenarioScope
@Component
class DeviceEventsScenarioContext(
    properties: DeviceEventsConfigurationProperties,
) {
    var inboundEventType: EventType? = null
    var inboundEventMessage: DeviceEventMessage? = null
    var outboundEventMessage: RequestMessage? = null

    val inboundQueue: String = properties.consumer.inboundQueue
    val outboundQueue: String = properties.producer.outboundQueue
}
