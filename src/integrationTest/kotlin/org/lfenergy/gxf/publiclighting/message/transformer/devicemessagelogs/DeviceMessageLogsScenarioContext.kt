// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs

import io.cucumber.spring.ScenarioScope
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.LogItemMessage
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.config.DeviceMessageLogsConfigurationProperties
import org.springframework.stereotype.Component

@ScenarioScope
@Component
class DeviceMessageLogsScenarioContext(
    properties: DeviceMessageLogsConfigurationProperties,
) {
    var inboundLogItemMessage: LogItemMessage? = null
    var outboundLogItemMessage: ObjectMessage? = null

    val inboundQueue: String = properties.consumer.inboundQueue
    val outboundQueue: String = properties.producer.outboundQueue
}
