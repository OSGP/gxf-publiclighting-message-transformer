// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class ResumeScheduleMessageDataContainerDto(
    val index: Int? = null,
    val immediate: Boolean = true,
) : Serializable {
    companion object {
        private const val serialVersionUID = 4989992501170383172L
    }
}
