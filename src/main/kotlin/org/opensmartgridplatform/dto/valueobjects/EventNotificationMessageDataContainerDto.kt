// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

class EventNotificationMessageDataContainerDto(
    val eventNotifications: List<EventNotificationTypeDto>,
) : Serializable {
    companion object {
        private const val serialVersionUID = 4707772459625804068L
    }
}
