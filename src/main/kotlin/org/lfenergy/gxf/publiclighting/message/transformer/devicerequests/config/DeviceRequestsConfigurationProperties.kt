// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-requests")
data class DeviceRequestsConfigurationProperties(
    var enabled: Boolean,
    var consumer: ConsumerProperties,
    var producer: ProducerProperties,
) {
    data class ConsumerProperties(
        var inboundQueue: String,
        var minConcurrency: Int,
        var maxConcurrency: Int,
    )

    data class ProducerProperties(
        var outboundQueue: String,
        var qualityOfService: QualityOfService,
    )

    data class QualityOfService(
        /** when set to true enables the use of deliveryMode, priority, and timeToLive */
        var explicitQosEnabled: Boolean,
        /** when set to true enables persistent message delivery */
        var deliveryPersistent: Boolean,
        /** value between 0 (lowest) and 9 (highest), default is 4 */
        var priority: Int,
        /** time to live in milliseconds, 0 means messages never expire */
        var timeToLive: Long,
    )
}
