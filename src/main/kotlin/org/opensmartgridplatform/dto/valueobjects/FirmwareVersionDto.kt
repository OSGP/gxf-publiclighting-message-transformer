// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class FirmwareVersionDto(
    val firmwareModuleType: FirmwareModuleType?,
    val version: String?,
) : Serializable {
    companion object {
        private const val serialVersionUID = 4842058824665590962L
    }
}
