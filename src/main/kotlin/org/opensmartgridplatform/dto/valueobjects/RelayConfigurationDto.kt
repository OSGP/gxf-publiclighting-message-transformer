// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class RelayConfigurationDto(
    val relayMap: MutableList<RelayMapDto?>? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = -108654955320491314L
    }
}
