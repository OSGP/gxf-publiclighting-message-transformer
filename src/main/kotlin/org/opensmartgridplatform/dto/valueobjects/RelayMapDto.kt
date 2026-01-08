// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class RelayMapDto(
    val index: Int? = null,
    val address: Int? = null,
    val relayType: RelayTypeDto? = null,
    val alias: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = -8744650092009418556L
    }
}
