// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class DeviceFixedIpDto(
    val ipAddress: String? = null,
    val netMask: String? = null,
    val gateWay: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 8329456121046440627L
    }
}
