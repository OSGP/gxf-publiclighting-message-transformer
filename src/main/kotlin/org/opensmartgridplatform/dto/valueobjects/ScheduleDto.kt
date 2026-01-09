// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class ScheduleDto(
    val astronomicalSunriseOffset: Short? = null,
    val astronomicalSunsetOffset: Short? = null,
    val scheduleList: MutableList<ScheduleEntryDto?>? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 6516779611853805357L
    }
}
