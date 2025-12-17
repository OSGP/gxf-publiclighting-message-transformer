// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import com.google.protobuf.ByteString
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.Configuration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.DeviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayLinkMatrix
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayLinking
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayMap
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayMapping
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.GetConfigurationResponse
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.DeviceFixedIpDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.dto.valueobjects.RelayConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.RelayMapDto
import org.opensmartgridplatform.dto.valueobjects.RelayMatrixDto
import org.opensmartgridplatform.dto.valueobjects.RelayTypeDto
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

object GetConfigurationResponseMapper {
    fun GetConfigurationResponse.toDto() = this.configuration.toDto()

    private fun Configuration.toDto(): ConfigurationDto =
        ConfigurationDto(
            // astronomical offsets configuration
            astroGateSunRiseOffset = this.astronomicalOffsetsConfiguration.sunriseOffset,
            astroGateSunSetOffset = this.astronomicalOffsetsConfiguration.sunsetOffset,
            // communication configuration
            communicationTimeout = this.communicationConfiguration.connectionTimeout,
            communicationNumberOfRetries = this.communicationConfiguration.numberOfRetries,
            communicationPauseTimeBetweenConnectionTrials = this.communicationConfiguration.delayBetweenConnectionAttempts,
            preferredLinkType = this.communicationConfiguration.preferredLinkType.toDto(),
            // daylight savings configuration
            automaticSummerTimingEnabled = this.daylightSavingsTimeConfiguration.automaticSummerTimingEnabled,
            summerTimeDetails = this.daylightSavingsTimeConfiguration.summerTimeDetails.toZonedDateTime(),
            winterTimeDetails = this.daylightSavingsTimeConfiguration.winterTimeDetails.toZonedDateTime(),
            // device address configuration
            deviceFixedIp = this.deviceAddressConfiguration.toDto(),
            dhcpEnabled = this.deviceAddressConfiguration.dhcpEnabled,
            // platform address configuration
            osgpIpAddress = this.platformAddressConfiguration.ipAddress.toIpv4String(),
            osgpPortNumber = this.platformAddressConfiguration.portNumber,
            // relay configuration
            relayConfiguration = this.relayConfiguration.relayMapping.toDto(),
            relayLinking = this.relayConfiguration.relayLinking.toDto(),
            relayRefreshing = this.relayConfiguration.relayRefreshingEnabled,
            // others
            lightType = this.lightType.toDto(),
            // TODO - Map switching delays
            switchingDelays = null,
            testButtonEnabled = this.testButtonEnabled,
            timeSyncFrequency = this.timeSyncFrequency,
        )

    private fun ByteString.toIpv4String(): String? {
        if (size() != 4) return null
        return toByteArray().joinToString(".") { (it.toInt() and 0xFF).toString() }
    }

    private fun ByteString.toIntOrNull(): Int? {
        val bytes = toByteArray()
        if (bytes.isEmpty()) return null
        return bytes[0].toInt() and 0xFF
    }

    private fun ByteString.toListOfIntOrNull(): MutableList<Int?>? {
        val bytes = toByteArray()
        if (bytes.isEmpty()) return null
        return bytes.map { it.toInt() and 0xFF }.toMutableList()
    }

    private fun DeviceAddressConfiguration.toDto() =
        DeviceFixedIpDto(
            this.ipAddress.toIpv4String(),
            netMask = this.netMask.toIpv4String(),
            gateWay = this.gateway.toIpv4String(),
        )

    private fun LightType.toDto() =
        when (this) {
            LightType.RELAY -> LightTypeDto.RELAY
            else -> null
        }

    private fun LinkType.toDto() =
        when (this) {
            LinkType.CDMA -> LinkTypeDto.CDMA
            LinkType.ETHERNET -> LinkTypeDto.ETHERNET
            LinkType.GPRS -> LinkTypeDto.GPRS
            else -> null
        }

    private fun RelayLinking.toDto(): MutableList<RelayMatrixDto?> = relayLinkMatrixList.map { it.toDto() }.toMutableList()

    private fun RelayLinkMatrix.toDto(): RelayMatrixDto =
        RelayMatrixDto(
            masterRelayIndex = masterRelayIndex.toIntOrNull(),
            masterRelayOn = masterRelayOn,
            indicesOfControlledRelaysOn = indicesOfControlledRelaysOn.toListOfIntOrNull(),
            indicesOfControlledRelaysOff = indicesOfControlledRelaysOff.toListOfIntOrNull(),
        )

    private fun RelayMap.toDto(): RelayMapDto =
        RelayMapDto(
            index = index.toIntOrNull(),
            address = address.toIntOrNull(),
            relayType = relayType.toDto(),
        )

    private fun RelayMapping.toDto() = RelayConfigurationDto(this.relayMapList.map { it.toDto() }.toMutableList())

    private fun RelayType.toDto() =
        when (this) {
            RelayType.LIGHT -> RelayTypeDto.LIGHT
            else -> null
        }

    /**
     * Converts a string like "0360100" to a ZonedDateTime in UTC.
     * Format: MMdHHmm, where:
     *   MM = month (e.g., 03 for March)
     *   d  = day-of-week indicator (0 means monday, 6 means Sunday)
     *   HH = hour (UTC)
     *   mm = minute (UTC)
     */
    private fun String.toZonedDateTime(): ZonedDateTime? {
        val dayOfWeekOffset = 1 // java.time uses 1 to 7 for day of week
        if (length != 7) return null
        val year = ZonedDateTime.now(ZoneOffset.UTC).year
        val month = substring(0, 2).toInt()
        val dayOfWeek = DayOfWeek.of(substring(2, 3).toInt() + dayOfWeekOffset)
        val hour = substring(3, 5).toInt()
        val minute = substring(5, 7).toInt()
        val lastDayOfMonth = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())
        val lastSpecificDay = lastDayOfMonth.with(TemporalAdjusters.previousOrSame(dayOfWeek))
        return lastSpecificDay.atTime(hour, minute).atZone(ZoneOffset.UTC)
    }
}
