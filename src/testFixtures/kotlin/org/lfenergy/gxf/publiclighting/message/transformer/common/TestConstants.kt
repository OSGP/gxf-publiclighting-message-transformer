// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.common

import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.FirmwareType
import org.opensmartgridplatform.dto.valueobjects.FirmwareModuleType
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto

object TestConstants {
    const val DEVICE_IDENTIFICATION = "device1"
    const val CORRELATION_UID = "corr1"
    const val ORGANIZATION_IDENTIFICATION = "org1"
    const val NETWORK_ADDRESS = "127.0.0.1"
    const val UNRECOGNIZED_VALUE = -1

    const val DOMAIN = "PUBLIC_LIGHTING"
    const val DOMAIN_VERSION = "1.0"

    val FIRMWARE_TYPE_DTO = FirmwareModuleType.FUNCTIONAL
    val FIRMWARE_TYPE = FirmwareType.FUNCTIONAL
    const val FIRMWARE_VERSION = "0.9.0"

    const val RETRY_COUNT = 0

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

    object StatusConstants {
        val LINK_TYPE = LinkType.ETHERNET
        val LINK_TYPE_DTO = LinkTypeDto.ETHERNET

        val LIGHT_TYPE_DTO = LightTypeDto.RELAY
    }
}
