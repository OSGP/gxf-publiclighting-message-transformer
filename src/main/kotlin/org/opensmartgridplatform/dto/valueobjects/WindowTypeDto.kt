// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class WindowTypeDto(
    val minutesBefore: Long = 0,
    val minutesAfter: Long = 0,
) : Serializable {
    companion object {
        private const val serialVersionUID = -6875513618481725063L
    }
}
