// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain

import java.io.Serializable

data class DeviceRegistrationDataDto(
    val ipAddress: String?,
    val deviceType: String?,
    val hasSchedule: Boolean?,
) : Serializable {
    companion object {
        private const val serialVersionUID = -5264884250167476931L
    }
}
