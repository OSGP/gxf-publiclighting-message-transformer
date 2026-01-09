// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import io.cucumber.spring.ScenarioScope
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config.DeviceRequestsConfigurationProperties
import org.opensmartgridplatform.shared.infra.jms.RequestMessage
import org.springframework.stereotype.Component

@ScenarioScope
@Component
class DeviceRequestsScenarioContext(
    properties: DeviceRequestsConfigurationProperties,
) {
    var inboundRequestType: ObjectMessageType? = null
    var inboundRequestMessage: RequestMessage? = null
    var outboundRequestMessage: DeviceRequestMessage? = null

    val inboundQueue: String = properties.consumer.inboundQueue
    val outboundQueue: String = properties.producer.outboundQueue
}
