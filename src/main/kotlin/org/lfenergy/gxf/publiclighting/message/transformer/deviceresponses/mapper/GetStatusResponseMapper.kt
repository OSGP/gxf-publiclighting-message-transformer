// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.GetStatusResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.RelayIndex
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto

object GetStatusResponseMapper {
    fun GetStatusResponse.toDto(): DeviceStatusDto =
        DeviceStatusDto(
            this.lightValuesList.toDto(),
            this.preferredLinkType.toDto(),
            this.actualLinkType.toDto(),
            this.lightType.toDto(),
            this.eventNotificationMask,
            this.numberOfOutputs,
            this.dcOutputVoltageMaximum,
            this.dcOutputVoltageCurrent,
            this.maximumOutputPowerOnDcOutput,
            this.serialNumber.toStringUtf8(),
            this.macAddress.toStringUtf8(),
            this.hardwareId,
            this.internalFlashMemSize,
            this.externalFlashMemSize,
            this.lastInternalTestResultCode,
            this.startupCounter,
            this.bootLoaderVersion,
            this.firmwareVersion,
            this.currentConfigurationBackUsed.toStringUtf8(),
            this.name,
            this.currentTime,
            this.currentIp,
        )

    private fun List<LightValue>.toDto(): MutableList<LightValueDto?> =
        filterNotNull().map { LightValueDto(it.index.toInt(), it.lightOn, null) }.toMutableList()

    private fun RelayIndex.toInt() =
        when (this) {
            RelayIndex.RELAY_ALL -> 0
            RelayIndex.RELAY_ONE -> 1
            RelayIndex.RELAY_TWO -> 2
            RelayIndex.RELAY_THREE -> 3
            RelayIndex.RELAY_FOUR -> 4
            else -> throw IllegalArgumentException("Unsupported relay index: $this")
        }

    private fun LinkType.toDto() =
        when (this) {
            LinkType.ETHERNET -> LinkTypeDto.ETHERNET
            LinkType.CDMA -> LinkTypeDto.CDMA
            LinkType.GPRS -> LinkTypeDto.GPRS
            else -> throw IllegalArgumentException("Unsupported link type: $this")
        }

    private fun LightType.toDto() =
        when (this) {
            LightType.RELAY -> LightTypeDto.RELAY
            // Other enum values not used
            else -> throw IllegalArgumentException("Unsupported light type: $this")
        }
}
