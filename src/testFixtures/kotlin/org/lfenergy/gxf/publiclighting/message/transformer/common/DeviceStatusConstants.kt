// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.common

import com.google.protobuf.ByteString
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.lightValue
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.RELAY_FOUR
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.RELAY_ONE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.RELAY_THREE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.RELAY_TWO
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto

object DeviceStatusConstants {
    val LIGHT_VALUES =
        listOf<LightValue>(
            lightValue {
                index = RelayIndex.RELAY_ONE
                lightOn = false
            },
            lightValue {

                index = RelayIndex.RELAY_TWO
                lightOn = true
            },
            lightValue {
                index = RelayIndex.RELAY_THREE
                lightOn = true
            },
            lightValue {
                index = RelayIndex.RELAY_FOUR
                lightOn = true
            },
        )

    val LIGHT_VALUES_DTO =
        mutableListOf<LightValueDto?>(
            LightValueDto(RELAY_ONE, false, null),
            LightValueDto(RELAY_TWO, true, null),
            LightValueDto(RELAY_THREE, true, null),
            LightValueDto(RELAY_FOUR, true, null),
        )

    val PREFERRED_LINK_TYPE = LinkType.ETHERNET
    val PREFERRED_LINK_TYPE_DTO = LinkTypeDto.ETHERNET

    val ACTUAL_LINK_TYPE = LinkType.CDMA
    val ACTUAL_LINK_TYPE_DTO = LinkTypeDto.CDMA

    val LIGHT_TYPE = LightType.RELAY
    val LIGHT_TYPE_DTO = LightTypeDto.RELAY
    const val EVENT_NOTIFICATIONS_MASK = 1
    const val NUMBER_OF_OUTPUTS = 3

    // TODO - Update to more realistic values
    const val DC_OUTPUT_VOLTAGE_MAXIMUM = 150
    const val DC_OUTPUT_VOLTAGE_CURRENT = 100
    const val MAXIMUM_OUTPUT_POWER_ON_DC_OUTPUT = 50

    const val SERIAL_NUMBER = "AAA0002025"
    val SERIAL_NUMBER_BYTES = SERIAL_NUMBER.toByteString()
    const val MAC_ADDRESS = "AA:BB:CC:DD"
    val MAC_ADDRESS_BYTES = MAC_ADDRESS.toByteString()
    const val HARDWARE_ID = "HARDWARE_ID"
    const val INTERNAL_FLASH_MEM_SIZE = 16
    const val EXTERNAL_FLASH_MEM_SIZE = 32
    const val LAST_INTERNAL_TEST_RESULT_CODE = 0
    const val STARTUP_COUNTER = 1
    const val BOOT_LOADER_VERSION = "0.1.0"
    const val FIRMWARE_VERSION = "0.2.0"
    const val CURRENT_CONFIGURATION_BACK_USED = "x"
    val CURRENT_CONFIGURATION_BACK_USED_BYTES = CURRENT_CONFIGURATION_BACK_USED.toByteString()
    const val NAME = "NAME"
    const val CURRENT_TIME = "202501011500"
    const val CURRENT_IP = "192.168.2.2"

    fun String.toByteString(): ByteString = ByteString.copyFrom(toByteArray())
}
