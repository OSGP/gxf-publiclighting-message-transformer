// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

enum class ObjectMessageType {
    UNRECOGNIZED,
    GET_FIRMWARE_VERSION,
    GET_STATUS,
    RESUME_SCHEDULE,
    SET_EVENT_NOTIFICATIONS,
    SET_LIGHT,
    SET_REBOOT,
    SET_SCHEDULE,
    SET_TRANSITION,
    START_SELF_TEST,
    STOP_SELF_TEST,
}
