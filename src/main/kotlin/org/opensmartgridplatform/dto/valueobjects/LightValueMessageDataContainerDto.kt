// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class LightValueMessageDataContainerDto(
    val lightValues: List<LightValueDto>,
) : Serializable {
    companion object {
        private const val serialVersionUID = 4725254533964342905L
    }
}
