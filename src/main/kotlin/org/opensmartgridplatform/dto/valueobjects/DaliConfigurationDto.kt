// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class DaliConfigurationDto(
    val numberOfLights: Int? = null,
    val indexAddressMap: MutableMap<Int?, Int?>? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = -3988541249244724989L
    }
}
