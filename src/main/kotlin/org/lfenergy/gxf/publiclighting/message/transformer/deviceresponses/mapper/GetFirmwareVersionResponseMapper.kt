// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.FirmwareType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.GetFirmwareVersionResponse
import org.opensmartgridplatform.dto.valueobjects.FirmwareModuleType
import org.opensmartgridplatform.dto.valueobjects.FirmwareVersionDto
import java.io.Serializable

object GetFirmwareVersionResponseMapper {
    fun GetFirmwareVersionResponse.toDto(): java.io.Serializable =
        this.firmwareVersionsList.map {
            FirmwareVersionDto(
                it.firmwareType.toDto(),
                it.version,
            )
        } as Serializable

    private fun FirmwareType.toDto(): FirmwareModuleType =
        when (this) {
            FirmwareType.COMMUNICATION -> FirmwareModuleType.COMMUNICATION
            FirmwareType.FUNCTIONAL -> FirmwareModuleType.FUNCTIONAL
            FirmwareType.SECURITY -> FirmwareModuleType.SECURITY
            else -> throw IllegalArgumentException("Unsupported firmware type: $this")
        }
}
