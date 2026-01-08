// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable
import java.time.ZonedDateTime

class EventNotificationDto(
    val deviceUid: String?,
    val dateTime: ZonedDateTime?,
    val eventType: EventTypeDto?,
    val description: String?,
    val index: Int?,
) : Serializable {
    companion object {
        private const val serialVersionUID = 5665838352689024852L
    }
}
