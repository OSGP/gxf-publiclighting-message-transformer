// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.assertj.core.api.Assertions.assertThat
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.dto.valueobjects.FirmwareVersionDto
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage

object DeviceResponseTestHelper {
    val payloadPerResponseTypeAssertions: Map<ResponseType, ProtocolResponseMessage.() -> Unit> =
        mapOf(
            ResponseType.GET_CONFIGURATION_RESPONSE to { verifyConfigurationDto() },
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE to { verifyFirmwareVersionDtoList() },
            ResponseType.GET_LIGHT_STATUS_RESPONSE to { verifyDeviceStatusDto() },
            ResponseType.GET_STATUS_RESPONSE to { verifyDeviceStatusDto() },
            ResponseType.REBOOT_RESPONSE to { verifyNoDataObject() },
            ResponseType.RESUME_SCHEDULE_RESPONSE to { verifyNoDataObject() },
            ResponseType.SET_CONFIGURATION_RESPONSE to { verifyNoDataObject() },
            ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE to { verifyNoDataObject() },
            ResponseType.SET_LIGHT_RESPONSE to { verifyNoDataObject() },
            ResponseType.SET_SCHEDULE_RESPONSE to { verifyNoDataObject() },
            ResponseType.SET_TRANSITION_RESPONSE to { verifyNoDataObject() },
            ResponseType.START_SELF_TEST_RESPONSE to { verifyNoDataObject() },
            ResponseType.STOP_SELF_TEST_RESPONSE to { verifyNoDataObject() },
        )

    private fun ProtocolResponseMessage.verifyConfigurationDto() {
        assertThat(dataObject).isNotNull.isInstanceOf(ConfigurationDto::class.java)
    }

    private fun ProtocolResponseMessage.verifyFirmwareVersionDtoList() {
        assertThat(dataObject).isNotNull().isInstanceOf(List::class.java)
        val firmwareVersions = dataObject as List<*>
        assertThat(firmwareVersions).isNotEmpty.hasSize(1)
        assertThat(firmwareVersions[0]).isInstanceOf(FirmwareVersionDto::class.java)
    }

    private fun ProtocolResponseMessage.verifyDeviceStatusDto() {
        assertThat(dataObject).isNotNull.isInstanceOf(DeviceStatusDto::class.java)
    }

    private fun ProtocolResponseMessage.verifyNoDataObject() {
        assertThat(dataObject).isNull()
    }
}
