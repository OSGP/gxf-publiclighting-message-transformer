// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

enum class FirmwareModuleType(
    val description: String,
) {
    COMMUNICATION("COMMUNICATION_MODULE_ACTIVE_FIRMWARE"),
    FUNCTIONAL("Functional"),
    SECURITY("Security"),
    M_BUS("M-bus"),
    MODULE_ACTIVE("MODULE_ACTIVE_FIRMWARE"),
    ACTIVE_FIRMWARE("ACTIVE_FIRMWARE"),
    M_BUS_DRIVER_ACTIVE("M_BUS_DRIVER_ACTIVE_FIRMWARE"),
    SIMPLE_VERSION_INFO("SIMPLE_VERSION_INFO"),
}
