// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class RelayMatrixDto(
    val masterRelayIndex: Int? = null,
    val masterRelayOn: Boolean = false,
    val indicesOfControlledRelaysOn: MutableList<Int?>? = null,
    val indicesOfControlledRelaysOff: MutableList<Int?>? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1679416098090362861L
    }
}
