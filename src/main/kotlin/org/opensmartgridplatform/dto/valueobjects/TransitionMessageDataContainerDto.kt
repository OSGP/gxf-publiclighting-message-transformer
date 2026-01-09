// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable
import java.time.ZonedDateTime

data class TransitionMessageDataContainerDto(
    val transitionType: TransitionTypeDto?,
    val dateTime: ZonedDateTime?,
) : Serializable {
    companion object {
        private const val serialVersionUID = 5491018613060059335L
    }
}
