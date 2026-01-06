// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import com.google.protobuf.ByteString
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.CommunicationConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.DaylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.astronomicalOffsetsConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.communicationConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.configuration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.daylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.deviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.platformAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayLinkMatrix
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayLinking
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayMap
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayMapping
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetConfigurationRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setConfigurationRequest
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.CommonMappingFunctions.toByteString
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.dto.valueobjects.RelayMapDto
import org.opensmartgridplatform.dto.valueobjects.RelayMatrixDto
import java.time.ZonedDateTime

object SetConfigurationRequestMapper {
    fun ConfigurationDto.toProtobufMessage(): SetConfigurationRequest {
        val dto = this
        return setConfigurationRequest {
            configuration =
                configuration {
                    toAstronomicalOffsetsConfigurationOrNull()?.let { astronomicalOffsetsConfiguration = it }
                    toCommunicationConfigurationOrNull()?.let { communicationConfiguration = it }
                    toDaylightSavingsTimeConfigurationOrNull()?.let { daylightSavingsTimeConfiguration = it }
                    toDeviceAddressConfigurationOrNull()?.let { deviceAddressConfiguration = it }
                    toPlatformAddressConfigurationOrNull()?.let { platformAddressConfiguration = it }
                    toRelayConfigurationOrNull()?.let { relayConfiguration = it }
                    dto.lightType?.let { lightType = it.toProtobuf() }
                    dto.testButtonEnabled?.let { testButtonEnabled = it }
                    dto.timeSyncFrequency?.let { timeSyncFrequency = it }
                    dto.switchingDelays?.let { switchingDelay.addAll(it.filterNotNull()) }
                }
        }
    }

    private fun ConfigurationDto.toAstronomicalOffsetsConfigurationOrNull() =
        if (astroGateSunSetOffset != null || astroGateSunRiseOffset != null) toAstronomicalOffsetsConfiguration() else null

    private fun ConfigurationDto.toCommunicationConfigurationOrNull() =
        if (communicationTimeout != null ||
            communicationNumberOfRetries != null ||
            communicationPauseTimeBetweenConnectionTrials != null
        ) {
            toCommunicationConfiguration()
        } else {
            null
        }

    private fun ConfigurationDto.toDaylightSavingsTimeConfigurationOrNull() =
        if (automaticSummerTimingEnabled != null ||
            summerTimeDetails != null ||
            winterTimeDetails != null
        ) {
            toDaylightSavingsTimeConfiguration()
        } else {
            null
        }

    private fun ConfigurationDto.toDeviceAddressConfigurationOrNull() =
        if (deviceFixedIp != null || dhcpEnabled != null) toDeviceAddressConfiguration() else null

    private fun ConfigurationDto.toPlatformAddressConfigurationOrNull() =
        if (osgpIpAddress != null || osgpPortNumber != null) toPlatformAddressConfiguration() else null

    private fun ConfigurationDto.toRelayConfigurationOrNull() =
        if (relayRefreshing != null ||
            relayConfiguration != null ||
            relayLinking != null
        ) {
            toRelayConfiguration()
        } else {
            null
        }

    private fun ConfigurationDto.toAstronomicalOffsetsConfiguration() =
        astronomicalOffsetsConfiguration {
            astroGateSunSetOffset?.let { sunsetOffset = it }
            astroGateSunRiseOffset?.let { sunriseOffset = it }
        }

    private fun ConfigurationDto.toCommunicationConfiguration(): CommunicationConfiguration {
        val dto = this
        return communicationConfiguration {
            dto.preferredLinkType?.let { preferredLinkType = it.toProtobuf() }
            communicationTimeout?.let { connectionTimeout = it }
            communicationNumberOfRetries?.let { numberOfRetries = it }
            communicationPauseTimeBetweenConnectionTrials?.let { delayBetweenConnectionAttempts = it }
        }
    }

    private fun ConfigurationDto.toDaylightSavingsTimeConfiguration(): DaylightSavingsTimeConfiguration {
        val dto = this
        return daylightSavingsTimeConfiguration {
            dto.automaticSummerTimingEnabled?.let { automaticSummerTimingEnabled = it }
            dto.summerTimeDetails?.let { summerTimeDetails = it.toProtobufDaylightSavingsDetail() }
            dto.winterTimeDetails?.let { winterTimeDetails = it.toProtobufDaylightSavingsDetail() }
        }
    }

    private fun ConfigurationDto.toDeviceAddressConfiguration() =
        deviceAddressConfiguration {
            deviceFixedIp?.ipAddress?.let { ipAddress = it.toIpByteString() }
            deviceFixedIp?.netMask?.let { netMask = it.toIpByteString() }
            deviceFixedIp?.gateWay?.let { gateway = it.toIpByteString() }
        }

    private fun ConfigurationDto.toPlatformAddressConfiguration() =
        platformAddressConfiguration {
            osgpIpAddress?.let { ipAddress = osgpIpAddress.toIpByteString() }
            osgpPortNumber?.let { portNumber = osgpPortNumber }
        }

    private fun ConfigurationDto.toRelayConfiguration(): RelayConfiguration {
        val dto = this
        return relayConfiguration {
            if (dto.hasRelayMapping()) {
                relayMapping = dto.relayConfiguration!!.relayMap!!.toRelayMapping()
            }
            if (dto.hasRelayLinking()) {
                relayLinking = dto.relayLinking!!.toRelayLinking()
            }
            dto.relayRefreshing?.let { relayRefreshingEnabled = it }
        }
    }

    private fun ConfigurationDto.hasRelayMapping() = relayConfiguration != null && relayConfiguration.relayMap != null

    private fun MutableList<RelayMapDto?>.toRelayMapping() =
        relayMapping {
            relayMap.addAll(
                filterNotNull().map { relayMapDto ->
                    relayMap {
                        relayMapDto.index?.let { index = it.toByteString() }
                        relayMapDto.address?.let { address = it.toByteString() }
                        relayMapDto.relayType?.let { relayType = RelayType.valueOf(it.name) }
                    }
                },
            )
        }

    private fun ConfigurationDto.hasRelayLinking() = relayLinking != null

    private fun MutableList<RelayMatrixDto?>.toRelayLinking() =
        relayLinking {
            relayLinkMatrix.addAll(
                filterNotNull().map { relayLinkDto ->
                    relayLinkMatrix {
                        relayLinkDto.masterRelayIndex?.let { masterRelayIndex = it.toByteString() }
                        masterRelayOn = relayLinkDto.masterRelayOn
                        relayLinkDto.indicesOfControlledRelaysOn?.let {
                            indicesOfControlledRelaysOn = it.toByteString()
                        }
                        relayLinkDto.indicesOfControlledRelaysOff?.let {
                            indicesOfControlledRelaysOff = it.toByteString()
                        }
                    }
                },
            )
        }

    private fun ZonedDateTime.toProtobufDaylightSavingsDetail() =
        String.format("%02d", this.monthValue) +
            (this.dayOfWeek.value - 1) +
            String.format("%02d", this.hour) +
            String.format("%02d", this.minute)

    private fun LightTypeDto.toProtobuf() =
        when (this) {
            LightTypeDto.RELAY -> LightType.RELAY
            else -> LightType.LIGHT_TYPE_NOT_SET
        }

    private fun LinkTypeDto.toProtobuf() =
        when (this) {
            LinkTypeDto.CDMA -> LinkType.CDMA
            LinkTypeDto.ETHERNET -> LinkType.ETHERNET
            LinkTypeDto.GPRS -> LinkType.GPRS
        }

    fun String.toIpByteString(): ByteString =
        this
            .split(".")
            .map { it.toInt().toByte() }
            .let { ByteString.copyFrom(it.toByteArray()) }

    fun MutableList<Int?>.toByteString(): ByteString =
        this.filterNotNull().map { it.toByte() }.let { ByteString.copyFrom(it.toByteArray()) }
}
