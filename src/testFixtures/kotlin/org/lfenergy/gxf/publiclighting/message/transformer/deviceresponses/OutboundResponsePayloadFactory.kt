// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.DeviceStatusConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.DeviceFixedIpDto
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.dto.valueobjects.FirmwareVersionDto
import kotlin.collections.MutableList

object OutboundResponsePayloadFactory {
    fun configurationPayload(): ConfigurationDto =
        ConfigurationDto(
            lightType = ConfigurationConstants.LIGHT_TYPE_DTO_RELAY,
            relayConfiguration = ConfigurationConstants.RelayConfiguration.RELAY_CONFIGURATION_DTO,
            preferredLinkType = ConfigurationConstants.CommunicationConfiguration.LINK_TYPE_DTO,
            timeSyncFrequency = ConfigurationConstants.TIME_SYNC_FREQUENCY_IN_SECONDS,
            deviceFixedIp =
                DeviceFixedIpDto(
                    ConfigurationConstants.DeviceAddressConfiguration.IP_ADDRESS,
                    ConfigurationConstants.DeviceAddressConfiguration.NET_MASK,
                    ConfigurationConstants.DeviceAddressConfiguration.GATEWAY,
                ),
            dhcpEnabled = ConfigurationConstants.DeviceAddressConfiguration.DHCP_ENABLED,
            communicationTimeout = ConfigurationConstants.CommunicationConfiguration.CONNECTION_TIMEOUT_IN_SECONDS,
            communicationNumberOfRetries = ConfigurationConstants.CommunicationConfiguration.NUMBER_OF_RETRIES,
            communicationPauseTimeBetweenConnectionTrials =
                ConfigurationConstants.CommunicationConfiguration.DELAY_BETWEEN_CONNECTION_ATTEMPTS_IN_SECONDS,
            osgpIpAddress = ConfigurationConstants.PlatformAddressConfiguration.IP_ADDRESS,
            osgpPortNumber = ConfigurationConstants.PlatformAddressConfiguration.PORT,
            testButtonEnabled = ConfigurationConstants.TEST_BUTTON_ENABLED,
            automaticSummerTimingEnabled = ConfigurationConstants.DaylightSavingsTimeConfiguration.AUTO_ENABLED,
            astroGateSunRiseOffset = ConfigurationConstants.AstronomicalOffsetConfiguration.SUNRISE_OFFSET_IN_SECONDS,
            astroGateSunSetOffset = ConfigurationConstants.AstronomicalOffsetConfiguration.SUNSET_OFFSET_IN_SECONDS,
            switchingDelays = ConfigurationConstants.SWITCHING_DELAYS,
            relayLinking = ConfigurationConstants.RelayConfiguration.RELAY_LINKING_DTO,
            relayRefreshing = ConfigurationConstants.RelayConfiguration.RELAY_REFRESHING_ENABLED,
            summerTimeDetails = ConfigurationConstants.DaylightSavingsTimeConfiguration.BEGIN_OF_DAYLIGHT_SAVINGS_TIME_DTO,
            winterTimeDetails = ConfigurationConstants.DaylightSavingsTimeConfiguration.END_OF_DAYLIGHT_SAVINGS_TIME_DTO,
        )

    fun statusPayload(): DeviceStatusDto =
        DeviceStatusDto(
            lightValues = DeviceStatusConstants.LIGHT_VALUES_DTO,
            preferredLinkType = DeviceStatusConstants.PREFERRED_LINK_TYPE_DTO,
            actualLinkType = DeviceStatusConstants.ACTUAL_LINK_TYPE_DTO,
            lightType = DeviceStatusConstants.LIGHT_TYPE_DTO,
            eventNotificationsMask = DeviceStatusConstants.EVENT_NOTIFICATIONS_MASK,
            numberOfOutputs = DeviceStatusConstants.NUMBER_OF_OUTPUTS,
            dcOutputVoltageMaximum = DeviceStatusConstants.DC_OUTPUT_VOLTAGE_MAXIMUM,
            dcOutputVoltageCurrent = DeviceStatusConstants.DC_OUTPUT_VOLTAGE_CURRENT,
            maximumOutputPowerOnDcOutput = DeviceStatusConstants.MAXIMUM_OUTPUT_POWER_ON_DC_OUTPUT,
            serialNumber = DeviceStatusConstants.SERIAL_NUMBER,
            macAddress = DeviceStatusConstants.MAC_ADDRESS,
            hardwareId = DeviceStatusConstants.HARDWARE_ID,
            internalFlashMemSize = DeviceStatusConstants.INTERNAL_FLASH_MEM_SIZE,
            externalFlashMemSize = DeviceStatusConstants.EXTERNAL_FLASH_MEM_SIZE,
            lastInternalTestResultCode = DeviceStatusConstants.LAST_INTERNAL_TEST_RESULT_CODE,
            startupCounter = DeviceStatusConstants.STARTUP_COUNTER,
            bootLoaderVersion = DeviceStatusConstants.BOOT_LOADER_VERSION,
            firmwareVersion = DeviceStatusConstants.FIRMWARE_VERSION,
            currentConfigurationBackUsed = DeviceStatusConstants.CURRENT_CONFIGURATION_BACK_USED,
            name = DeviceStatusConstants.NAME,
            currentTime = DeviceStatusConstants.CURRENT_TIME,
            currentIp = DeviceStatusConstants.CURRENT_IP,
        )

    fun firmwarePayload(): MutableList<FirmwareVersionDto?> =
        mutableListOf(FirmwareVersionDto(TestConstants.FIRMWARE_TYPE_DTO, TestConstants.FIRMWARE_VERSION))
}
