// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

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
import java.time.ZoneOffset
import java.time.ZonedDateTime

object TestConstants {
    const val DEVICE_IDENTIFICATION = "device1"
    const val CORRELATION_UID = "corr1"
    const val ORGANIZATION_IDENTIFICATION = "org1"
    const val NETWORK_ADDRESS = "127.0.0.1"
    const val UNRECOGNIZED_VALUE = -1

    const val DOMAIN = "PUBLIC_LIGHTING"
    const val DOMAIN_VERSION = "1.0"

    const val DEFAULT_PRIORITY = 4

    const val HALF_HOUR_IN_SECONDS = 30 * 60

    const val ALL_RELAYS = 0
    const val RELAY_ONE = 1
    const val RELAY_TWO = 2
    const val RELAY_THREE = 3
    const val RELAY_FOUR = 4

    const val ELEVEN_PM = "230000"
    const val SIX_AM = "060000"

//    const val CONNECTION_TIMEOUT = 120

    object ConfigurationConstants {
//        val LINK_TYPE_DTO_GPRS = LinkTypeDto.GPRS
//        val LINK_TYPE_PROTO_GPRS = LinkType.GPRS
        val LIGHT_TYPE_DTO_RELAY = LightTypeDto.RELAY
        val LIGHT_TYPE_PROTOC_RELAY = LightType.RELAY
        const val TEST_BUTTON_ENABLED = true
        const val TIME_SYNC_FREQUENCY_IN_SECONDS = 600
        val switchingDelays: MutableList<Int?> = mutableListOf(10, 20)

        // TODO - Not used?
//        daliConfiguration = null,
//        ntpEnabled = true,
//        ntpHost = "ntp-server",
//        ntpSyncInterval = 300,

        object AstronomicalOffsetConfiguration {
            const val SUNRISE_OFFSET_IN_SECONDS = -900 // 15 minutes
            const val SUNSET_OFFSET_IN_SECONDS = 900 // 15 minutes
        }

        object DaylightSavingsTimeConfiguration {
            const val AUTO_ENABLED = true

            // TODO - Is this local or UTC time? Local time is switched from 02:00 to 03:00, UTC time will remain 01:00
            val BEGIN_OF_DAYLIGHT_SAVINGS_TIME_DTO: ZonedDateTime = ZonedDateTime.of(2025, 3, 30, 1, 0, 0, 0, ZoneOffset.UTC)
            const val BEGIN_OF_DAYLIGHT_SAVINGS_TIME = "0360100" // Last sunday of March at 01:00 UTC

            // TODO - Is this correct? Shouldn't this be 01:00 UTC as well? Local time is switched back from 03:00 to 02:00, UTC time will then be 01:00
            val END_OF_DAYLIGHT_SAVINGS_TIME_DTO: ZonedDateTime = ZonedDateTime.of(2025, 10, 26, 2, 0, 0, 0, ZoneOffset.UTC)
            const val END_OF_DAYLIGHT_SAVINGS_TIME = "1060200" // Last sunday of October at 02:00 UTC
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
                        ),
                    )
                }

            val RELAY_LINKING_DTO =
                mutableListOf(
                    RelayMatrixDto(3, true, mutableListOf(4)),
                    RelayMatrixDto(3, false, mutableListOf(4)),
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
    }

    fun Int.toByteString(): ByteString {
        require(this in 1..4) { "Value must be between 1 and 4, but was $this" }
        return ByteString.copyFrom(byteArrayOf(this.toByte()))
    }

    fun String.toIpByteString(): ByteString =
        this
            .split(".")
            .map { it.toInt().toByte() }
            .let { ByteString.copyFrom(it.toByteArray()) }
}
