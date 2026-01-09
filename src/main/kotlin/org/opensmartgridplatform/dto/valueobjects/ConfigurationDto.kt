// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable
import java.time.ZonedDateTime

data class ConfigurationDto(
    val lightType: LightTypeDto? = null,
    val daliConfiguration: DaliConfigurationDto? = null,
    val relayConfiguration: RelayConfigurationDto? = null,
    val preferredLinkType: LinkTypeDto? = null,
    val timeSyncFrequency: Int? = null,
    val deviceFixedIp: DeviceFixedIpDto? = null,
    val dhcpEnabled: Boolean? = null,
    val tlsEnabled: Boolean? = null,
    val tlsPortNumber: Int? = null,
    val commonNameString: String? = null,
    val communicationTimeout: Int? = null,
    val communicationNumberOfRetries: Int? = null,
    val communicationPauseTimeBetweenConnectionTrials: Int? = null,
    val osgpIpAddress: String? = null,
    val osgpPortNumber: Int? = null,
    val ntpHost: String? = null,
    val ntpEnabled: Boolean? = null,
    val ntpSyncInterval: Int? = null,
    val testButtonEnabled: Boolean? = null,
    val automaticSummerTimingEnabled: Boolean? = null,
    val astroGateSunRiseOffset: Int? = null,
    val astroGateSunSetOffset: Int? = null,
    val switchingDelays: MutableList<Int?>? = null,
    val relayLinking: MutableList<RelayMatrixDto?>? = null,
    val relayRefreshing: Boolean? = null,
    val summerTimeDetails: ZonedDateTime? = null,
    val winterTimeDetails: ZonedDateTime? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 8359276160483972289L
    }
}
