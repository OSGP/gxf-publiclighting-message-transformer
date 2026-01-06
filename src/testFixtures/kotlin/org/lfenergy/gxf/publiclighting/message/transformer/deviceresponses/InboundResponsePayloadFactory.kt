// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.astronomicalOffsetsConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.communicationConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.configuration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.daylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.deviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.platformAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.firmwareVersion
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.getConfigurationResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.getFirmwareVersionResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.getStatusResponse
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.DeviceStatusConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants

object InboundResponsePayloadFactory {
    fun configurationPayload() =
        getConfigurationResponse {
            configuration =
                configuration {
                    astronomicalOffsetsConfiguration =
                        astronomicalOffsetsConfiguration {
                            sunriseOffset = ConfigurationConstants.AstronomicalOffsetConfiguration.SUNRISE_OFFSET_IN_SECONDS
                            sunsetOffset = ConfigurationConstants.AstronomicalOffsetConfiguration.SUNSET_OFFSET_IN_SECONDS
                        }
                    communicationConfiguration =
                        communicationConfiguration {
                            preferredLinkType = ConfigurationConstants.CommunicationConfiguration.LINK_TYPE
                            connectionTimeout = ConfigurationConstants.CommunicationConfiguration.CONNECTION_TIMEOUT_IN_SECONDS
                            numberOfRetries = ConfigurationConstants.CommunicationConfiguration.NUMBER_OF_RETRIES
                            delayBetweenConnectionAttempts =
                                ConfigurationConstants.CommunicationConfiguration.DELAY_BETWEEN_CONNECTION_ATTEMPTS_IN_SECONDS
                        }
                    daylightSavingsTimeConfiguration =
                        daylightSavingsTimeConfiguration {
                            automaticSummerTimingEnabled = ConfigurationConstants.DaylightSavingsTimeConfiguration.AUTO_ENABLED
                            summerTimeDetails =
                                ConfigurationConstants.DaylightSavingsTimeConfiguration.BEGIN_OF_DAYLIGHT_SAVINGS_TIME
                            winterTimeDetails = ConfigurationConstants.DaylightSavingsTimeConfiguration.END_OF_DAYLIGHT_SAVINGS_TIME
                        }
                    deviceAddressConfiguration =
                        deviceAddressConfiguration {
                            ipAddress = ConfigurationConstants.DeviceAddressConfiguration.IP_ADDRESS_BYTES
                            netMask = ConfigurationConstants.DeviceAddressConfiguration.NET_MASK_BYTES
                            gateway = ConfigurationConstants.DeviceAddressConfiguration.GATEWAY_BYTES
                            dhcpEnabled = ConfigurationConstants.DeviceAddressConfiguration.DHCP_ENABLED
                        }
                    platformAddressConfiguration =
                        platformAddressConfiguration {
                            ipAddress = ConfigurationConstants.PlatformAddressConfiguration.IP_ADDRESS_BYTES
                            portNumber = ConfigurationConstants.PlatformAddressConfiguration.PORT
                        }
                    relayConfiguration =
                        relayConfiguration {
                            relayRefreshingEnabled = ConfigurationConstants.RelayConfiguration.RELAY_REFRESHING_ENABLED
                            relayMapping = ConfigurationConstants.RelayConfiguration.RELAY_MAPPING
                            relayLinking = ConfigurationConstants.RelayConfiguration.RELAY_LINKING
                        }
                    lightType = ConfigurationConstants.LIGHT_TYPE_PROTOC_RELAY
                    testButtonEnabled = ConfigurationConstants.TEST_BUTTON_ENABLED
                    timeSyncFrequency = ConfigurationConstants.TIME_SYNC_FREQUENCY_IN_SECONDS
                    switchingDelay.addAll(ConfigurationConstants.SWITCHING_DELAYS.filterNotNull())
                }
        }

    fun statusPayload() =
        getStatusResponse {
            lightValues.addAll(DeviceStatusConstants.LIGHT_VALUES)
            preferredLinkType = DeviceStatusConstants.PREFERRED_LINK_TYPE
            actualLinkType = DeviceStatusConstants.ACTUAL_LINK_TYPE
            lightType = DeviceStatusConstants.LIGHT_TYPE
            eventNotificationMask = DeviceStatusConstants.EVENT_NOTIFICATIONS_MASK
            numberOfOutputs = DeviceStatusConstants.NUMBER_OF_OUTPUTS
            dcOutputVoltageMaximum = DeviceStatusConstants.DC_OUTPUT_VOLTAGE_MAXIMUM
            dcOutputVoltageCurrent = DeviceStatusConstants.DC_OUTPUT_VOLTAGE_CURRENT
            maximumOutputPowerOnDcOutput = DeviceStatusConstants.MAXIMUM_OUTPUT_POWER_ON_DC_OUTPUT
            serialNumber = DeviceStatusConstants.SERIAL_NUMBER_BYTES
            macAddress = DeviceStatusConstants.MAC_ADDRESS_BYTES
            hardwareId = DeviceStatusConstants.HARDWARE_ID
            internalFlashMemSize = DeviceStatusConstants.INTERNAL_FLASH_MEM_SIZE
            externalFlashMemSize = DeviceStatusConstants.EXTERNAL_FLASH_MEM_SIZE
            lastInternalTestResultCode = DeviceStatusConstants.LAST_INTERNAL_TEST_RESULT_CODE
            startupCounter = DeviceStatusConstants.STARTUP_COUNTER
            bootLoaderVersion = DeviceStatusConstants.BOOT_LOADER_VERSION
            firmwareVersion = DeviceStatusConstants.FIRMWARE_VERSION
            currentConfigurationBackUsed = DeviceStatusConstants.CURRENT_CONFIGURATION_BACK_USED_BYTES
            name = DeviceStatusConstants.NAME
            currentTime = DeviceStatusConstants.CURRENT_TIME
            currentIp = DeviceStatusConstants.CURRENT_IP
        }

    fun firmwareVersionsPayload() =
        getFirmwareVersionResponse {
            firmwareVersions.add(
                firmwareVersion {
                    firmwareType = TestConstants.FIRMWARE_TYPE
                    version = TestConstants.FIRMWARE_VERSION
                },
            )
        }
}
