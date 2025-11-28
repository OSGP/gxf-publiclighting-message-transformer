// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import io.cucumber.spring.ScenarioScope
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config.DeviceResponsesConfigurationProperties
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.springframework.stereotype.Component

@ScenarioScope
@Component
class DeviceResponsesScenarioContext(
    properties: DeviceResponsesConfigurationProperties,
) {
    var inboundResponseType: ResponseType? = null
    var inboundResponseMessage: DeviceResponseMessage? = null
    var outboundResponseMessage: ProtocolResponseMessage? = null

    val inboundQueue: String = properties.consumer.inboundQueue
    val outboundQueue: String = properties.producer.outboundQueue
}
