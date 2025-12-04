// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

enum class ObjectMessageType {
    UNRECOGNIZED,
    GET_STATUS,
    SET_LIGHT,
    SET_REBOOT,
    START_SELF_TEST,
    STOP_SELF_TEST,
    RESUME_SCHEDULE,
    SET_SCHEDULE,
}
