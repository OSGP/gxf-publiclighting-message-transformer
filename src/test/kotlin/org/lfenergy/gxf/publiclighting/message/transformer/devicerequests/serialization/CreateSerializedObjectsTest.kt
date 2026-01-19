// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.serialization

import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.message.transformer.common.DeviceStatusConstants
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.nio.file.Path

class CreateSerializedObjectsTest {
    /**
     * Serializes a {@link DeviceStatusResponse} object and writes it to a file named
     * code device-status-response.ser.
     *
     * <p>The generated file serves as a fixture for the
     * {@code ResponseDeserializationTest} in the
     * open-smart-grid-platform project, allowing that test to verify
     * deserialization compatibility across projects.
     */

    @Test
    fun deviceStatusResponse() {
        val lightValues = mutableListOf<LightValueDto?>(LightValueDto(index = 1, on = true, dimValue = null))
        val deviceStatusDto =
            DeviceStatusDto(
                lightValues,
                LinkTypeDto.ETHERNET,
                LinkTypeDto.ETHERNET,
                LightTypeDto.RELAY,
                DeviceStatusConstants.EVENT_NOTIFICATIONS_MASK,
                DeviceStatusConstants.NUMBER_OF_OUTPUTS,
                DeviceStatusConstants.DC_OUTPUT_VOLTAGE_MAXIMUM,
                DeviceStatusConstants.DC_OUTPUT_VOLTAGE_CURRENT,
                DeviceStatusConstants.MAXIMUM_OUTPUT_POWER_ON_DC_OUTPUT,
                DeviceStatusConstants.SERIAL_NUMBER,
                DeviceStatusConstants.MAC_ADDRESS,
                DeviceStatusConstants.HARDWARE_ID,
                DeviceStatusConstants.INTERNAL_FLASH_MEM_SIZE,
                DeviceStatusConstants.EXTERNAL_FLASH_MEM_SIZE,
                DeviceStatusConstants.LAST_INTERNAL_TEST_RESULT_CODE,
                DeviceStatusConstants.STARTUP_COUNTER,
                DeviceStatusConstants.BOOT_LOADER_VERSION,
                DeviceStatusConstants.FIRMWARE_VERSION,
                DeviceStatusConstants.CURRENT_CONFIGURATION_BACK_USED,
                DeviceStatusConstants.NAME,
                DeviceStatusConstants.CURRENT_TIME,
                DeviceStatusConstants.CURRENT_IP,
            )
        val response =
            ResponseMessage(
                deviceIdentification = "dvc_id",
                organisationIdentification = "org_id",
                correlationUid = "corr_id",
                messageType = "tst_msg",
                result = ResponseMessageResultType.OK,
                dataObject = deviceStatusDto,
            )
        createOutputFile(response)
    }

    private fun createOutputFile(response: ResponseMessage) {
        val output = Path.of("src", "test", "resources", "device-status-response.ser")
        try {
            ObjectOutputStream(FileOutputStream(output.toFile())).use { oos ->
                oos.writeObject(response.dataObject)
            }
        } catch (ex: Exception) {
            println("exception occurred: ${ex.message}")
        }
    }
}
