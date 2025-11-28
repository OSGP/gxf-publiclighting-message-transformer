// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import org.joda.time.DateTime
import java.io.Serializable

data class ScheduleEntryDto(
    val weekDay: WeekDayTypeDto? = null,
    val startDay: DateTime? = null,
    val endDay: DateTime? = null,
    val actionTime: ActionTimeTypeDto? = null,
    val time: String? = null,
    val triggerWindow: WindowTypeDto? = null,
    val lightValue: MutableList<LightValueDto?>? = null,
    val triggerType: TriggerTypeDto? = null,
    val index: Int? = null,
    val isEnabled: Boolean? = null,
    val minimumLightsOn: Int? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 7562344341386762082L
    }
}
