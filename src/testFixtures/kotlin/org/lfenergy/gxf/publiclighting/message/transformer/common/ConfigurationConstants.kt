// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.common

import com.google.protobuf.ByteString
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayLinkMatrix
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayLinking
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayMap
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayMapping
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.dto.valueobjects.RelayConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.RelayMapDto
import org.opensmartgridplatform.dto.valueobjects.RelayMatrixDto
import org.opensmartgridplatform.dto.valueobjects.RelayTypeDto
import java.time.DayOfWeek
import java.time.Month
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

object ConfigurationConstants {
    val LIGHT_TYPE_DTO_RELAY = LightTypeDto.RELAY
    val LIGHT_TYPE_PROTOC_RELAY = LightType.RELAY
    const val TEST_BUTTON_ENABLED = true
    const val TIME_SYNC_FREQUENCY_IN_SECONDS = 600
    val SWITCHING_DELAYS: MutableList<Int?> = mutableListOf(10, 20, 30, 40)

    object AstronomicalOffsetConfiguration {
        const val SUNRISE_OFFSET_IN_SECONDS = -900 // 15 minutes
        const val SUNSET_OFFSET_IN_SECONDS = 900 // 15 minutes
    }

    object DaylightSavingsTimeConfiguration {
        const val AUTO_ENABLED = true

        val BEGIN_OF_DAYLIGHT_SAVINGS_TIME_DTO: ZonedDateTime = lastSundayOfMonth(Month.MARCH)
        const val BEGIN_OF_DAYLIGHT_SAVINGS_TIME = "0360100" // Last sunday of March at 01:00 UTC

        val END_OF_DAYLIGHT_SAVINGS_TIME_DTO: ZonedDateTime = lastSundayOfMonth(Month.OCTOBER)
        const val END_OF_DAYLIGHT_SAVINGS_TIME = "1060100" // Last sunday of October at 01:00 UTC
    }

    object CommunicationConfiguration {
        const val NUMBER_OF_RETRIES = 3
        const val DELAY_BETWEEN_CONNECTION_ATTEMPTS_IN_SECONDS = 60
        const val CONNECTION_TIMEOUT_IN_SECONDS = 20
        val LINK_TYPE_DTO = LinkTypeDto.ETHERNET
        val LINK_TYPE = LinkType.ETHERNET
    }

    object DeviceAddressConfiguration {
        const val DHCP_ENABLED = true
        const val IP_ADDRESS = "192.168.2.2"
        val IP_ADDRESS_BYTES = IP_ADDRESS.toIpByteString()
        const val NET_MASK = "255.255.255.0"
        val NET_MASK_BYTES = NET_MASK.toIpByteString()
        const val GATEWAY = "192.168.2.1"
        val GATEWAY_BYTES = GATEWAY.toIpByteString()
    }

    object PlatformAddressConfiguration {
        const val IP_ADDRESS = "192.168.1.1"
        val IP_ADDRESS_BYTES = IP_ADDRESS.toIpByteString()
        const val PORT = 12125
    }

    object RelayConfiguration {
        val RELAY_CONFIGURATION_DTO =
            RelayConfigurationDto(
                mutableListOf(
                    RelayMapDto(2, 1, RelayTypeDto.LIGHT),
                    RelayMapDto(3, 2, RelayTypeDto.LIGHT),
                    RelayMapDto(4, 3, RelayTypeDto.LIGHT),
                ),
            )
        val RELAY_MAPPING =
            relayMapping {
                relayMap.addAll(
                    listOf(
                        relayMap {
                            index = 2.toByteString()
                            address = 1.toByteString()
                            relayType = RelayType.LIGHT
                        },
                        relayMap {
                            index = 3.toByteString()
                            address = 2.toByteString()
                            relayType = RelayType.LIGHT
                        },
                        relayMap {
                            index = 4.toByteString()
                            address = 3.toByteString()
                            relayType = RelayType.LIGHT
                        },
                    ),
                )
            }

        val RELAY_LINKING_DTO: MutableList<RelayMatrixDto?> =
            mutableListOf(
                RelayMatrixDto(3, true, mutableListOf(4), null),
                RelayMatrixDto(3, false, null, mutableListOf(4)),
            )

        val RELAY_LINKING =
            relayLinking {
                relayLinkMatrix.addAll(
                    listOf(
                        relayLinkMatrix {
                            masterRelayIndex = 3.toByteString()
                            masterRelayOn = true
                            indicesOfControlledRelaysOn = 4.toByteString()
                        },
                        relayLinkMatrix {
                            masterRelayIndex = 3.toByteString()
                            masterRelayOn = false
                            indicesOfControlledRelaysOff = 4.toByteString()
                        },
                    ),
                )
            }
        const val RELAY_REFRESHING_ENABLED = true
    }

    private fun Int.toByteString(): ByteString {
        require(this in 1..4) { "Value must be between 1 and 4, but was $this" }
        return ByteString.copyFrom(byteArrayOf(this.toByte()))
    }

    private fun String.toIpByteString(): ByteString =
        this
            .split(".")
            .map { it.toInt().toByte() }
            .let { ByteString.copyFrom(it.toByteArray()) }

    private fun lastSundayOfMonth(month: Month) =
        ZonedDateTime
            .now(ZoneOffset.UTC)
            .withMonth(month.value)
            .with(TemporalAdjusters.lastDayOfMonth())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .withHour(1)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
}
