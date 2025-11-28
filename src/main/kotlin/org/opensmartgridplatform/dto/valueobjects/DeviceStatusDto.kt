// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.dto.valueobjects

import java.io.Serializable

data class DeviceStatusDto(
    val lightValues: MutableList<LightValueDto?>? = null,
    val preferredLinkType: org.opensmartgridplatform.dto.valueobjects.LinkTypeDto? = null,
    val actualLinkType: org.opensmartgridplatform.dto.valueobjects.LinkTypeDto? = null,
    val lightType: org.opensmartgridplatform.dto.valueobjects.LightTypeDto? = null,
    val eventNotificationsMask: Int? = null,
    val numberOfOutputs: Int? = null,
    val dcOutputVoltageMaximum: Int? = null,
    val dcOutputVoltageCurrent: Int? = null,
    val maximumOutputPowerOnDcOutput: Int? = null,
    val serialNumber: String? = null,
    val macAddress: String? = null,
    val hardwareId: String? = null,
    val internalFlashMemSize: Int? = null,
    val externalFlashMemSize: Int? = null,
    val lastInternalTestResultCode: Int? = null,
    val startupCounter: Int? = null,
    val bootLoaderVersion: String? = null,
    val firmwareVersion: String? = null,
    val currentConfigurationBackUsed: String? = null,
    val name: String? = null,
    val currentTime: String? = null,
    val currentIp: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = -483312190851322867L
    }
}
