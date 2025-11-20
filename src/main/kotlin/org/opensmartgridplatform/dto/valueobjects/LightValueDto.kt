// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class LightValueDto(
    val index: Int?,
    val on: Boolean,
    val dimValue: Int?,
) : Serializable {
    companion object {
        private const val serialVersionUID = 3783788109559927722L
    }
}
