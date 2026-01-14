// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.assertj.core.api.Assertions.assertThat
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.AstronomicalOffsetsConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.CommunicationConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.Configuration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.DaylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.DeviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.PlatformAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ResumeScheduleRequest
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType

object DeviceRequestTestHelper {
    val payloadPerRequestTypeAssertions: Map<ObjectMessageType, DeviceRequestMessage.() -> Unit> =
        mapOf(
            ObjectMessageType.GET_CONFIGURATION to { verifyNoPayload() },
            ObjectMessageType.GET_FIRMWARE_VERSION to { verifyNoPayload() },
            ObjectMessageType.GET_LIGHT_STATUS to { verifyNoPayload() },
            ObjectMessageType.GET_STATUS to { verifyNoPayload() },
            ObjectMessageType.RESUME_SCHEDULE to { verifyResumeSchedulePayload() },
            ObjectMessageType.SET_CONFIGURATION to { verifySetConfigurationPayload() },
            ObjectMessageType.SET_EVENT_NOTIFICATIONS to { verifySetEventNotificationPayload() },
            ObjectMessageType.SET_LIGHT to { verifySetLightPayload() },
            ObjectMessageType.SET_REBOOT to { verifyNoPayload() },
            ObjectMessageType.SET_SCHEDULE to { verifySetSchedulePayload() },
            ObjectMessageType.SET_TRANSITION to { verifySetTransitionPayload() },
            ObjectMessageType.START_SELF_TEST to { verifyNoPayload() },
            ObjectMessageType.STOP_SELF_TEST to { verifyNoPayload() },
        )

    private fun DeviceRequestMessage.verifyNoPayload() {
        assertThat(hasResumeScheduleRequest()).isFalse
        assertThat(hasSetScheduleRequest()).isFalse
        assertThat(hasSetLightRequest()).isFalse
        assertThat(hasSetTransitionRequest()).isFalse
        assertThat(hasSetEventNotificationMaskRequest()).isFalse
    }

    fun DeviceRequestMessage.verifyResumeSchedulePayload() {
        assertThat(hasResumeScheduleRequest()).isTrue
        assertThat(resumeScheduleRequest).isInstanceOf(ResumeScheduleRequest::class.java)
    }

    private fun DeviceRequestMessage.verifySetConfigurationPayload() {
        assertThat(hasSetConfigurationRequest()).isTrue
        assertThat(setConfigurationRequest).isNotNull
        assertThat(setConfigurationRequest.configuration).isNotNull
        verifyConfigurationPayload(setConfigurationRequest.configuration)
    }

    private fun verifyConfigurationPayload(configuration: Configuration) {
        verifyAstronomicalOffsetConfiguration(configuration.astronomicalOffsetsConfiguration)
        verifyCommunicationConfiguration(configuration.communicationConfiguration)
        verifyDaylightSavingsTimeConfiguration(configuration.daylightSavingsTimeConfiguration)
        verifyDeviceAddressConfiguration(configuration.deviceAddressConfiguration)
        verifyPlatformAddressConfiguration(configuration.platformAddressConfiguration)
        verifyRelayConfiguration(configuration.relayConfiguration)

        assertThat(configuration.lightType).isEqualTo(ConfigurationConstants.LIGHT_TYPE_PROTOC_RELAY)
        assertThat(configuration.testButtonEnabled).isEqualTo(ConfigurationConstants.TEST_BUTTON_ENABLED)
        assertThat(configuration.timeSyncFrequency).isEqualTo(ConfigurationConstants.TIME_SYNC_FREQUENCY_IN_SECONDS)
        assertThat(configuration.switchingDelayList).isNotEmpty.isEqualTo(ConfigurationConstants.SWITCHING_DELAYS)
    }

    private fun verifyAstronomicalOffsetConfiguration(config: AstronomicalOffsetsConfiguration) {
        assertThat(config).isNotNull
        assertThat(config.sunsetOffset).isEqualTo(ConfigurationConstants.AstronomicalOffsetConfiguration.SUNSET_OFFSET_IN_SECONDS)
        assertThat(config.sunriseOffset).isEqualTo(ConfigurationConstants.AstronomicalOffsetConfiguration.SUNRISE_OFFSET_IN_SECONDS)
    }

    private fun verifyCommunicationConfiguration(config: CommunicationConfiguration) {
        assertThat(config).isNotNull
        assertThat(config.connectionTimeout).isEqualTo(ConfigurationConstants.CommunicationConfiguration.CONNECTION_TIMEOUT_IN_SECONDS)
        assertThat(config.numberOfRetries).isEqualTo(ConfigurationConstants.CommunicationConfiguration.NUMBER_OF_RETRIES)
        assertThat(
            config.delayBetweenConnectionAttempts,
        ).isEqualTo(ConfigurationConstants.CommunicationConfiguration.DELAY_BETWEEN_CONNECTION_ATTEMPTS_IN_SECONDS)
        assertThat(config.preferredLinkType).isEqualTo((ConfigurationConstants.CommunicationConfiguration.LINK_TYPE))
    }

    private fun verifyDaylightSavingsTimeConfiguration(config: DaylightSavingsTimeConfiguration) {
        assertThat(config).isNotNull
        assertThat(config.automaticSummerTimingEnabled).isEqualTo(ConfigurationConstants.DaylightSavingsTimeConfiguration.AUTO_ENABLED)
        assertThat(
            config.summerTimeDetails,
        ).isEqualTo(ConfigurationConstants.DaylightSavingsTimeConfiguration.BEGIN_OF_DAYLIGHT_SAVINGS_TIME)
        assertThat(config.winterTimeDetails).isEqualTo(ConfigurationConstants.DaylightSavingsTimeConfiguration.END_OF_DAYLIGHT_SAVINGS_TIME)
    }

    private fun verifyDeviceAddressConfiguration(config: DeviceAddressConfiguration) {
        assertThat(config.ipAddress).isEqualTo(ConfigurationConstants.DeviceAddressConfiguration.IP_ADDRESS_BYTES)
        assertThat(config.netMask).isEqualTo(ConfigurationConstants.DeviceAddressConfiguration.NET_MASK_BYTES)
        assertThat(config.gateway).isEqualTo(ConfigurationConstants.DeviceAddressConfiguration.GATEWAY_BYTES)
        assertThat(config.dhcpEnabled).isEqualTo(ConfigurationConstants.DeviceAddressConfiguration.DHCP_ENABLED)
    }

    private fun verifyPlatformAddressConfiguration(config: PlatformAddressConfiguration) {
        assertThat(config).isNotNull
        assertThat(config.ipAddress).isEqualTo(ConfigurationConstants.PlatformAddressConfiguration.IP_ADDRESS_BYTES)
        assertThat(config.portNumber).isEqualTo(ConfigurationConstants.PlatformAddressConfiguration.PORT)
    }

    private fun verifyRelayConfiguration(config: RelayConfiguration?) {
        assertThat(config).isNotNull
        assertThat(config!!.relayMapping).isNotNull
        with(config.relayMapping) {
            assertThat(relayMapList).isNotNull.hasSize(3)
        }
        assertThat(config.relayLinking).isNotNull
        with(config.relayLinking) {
            assertThat(relayLinkMatrixList).isNotNull.hasSize(2)
        }
    }

    private fun DeviceRequestMessage.verifySetEventNotificationPayload() {
        assertThat(hasSetEventNotificationMaskRequest()).isTrue
        assertThat(setEventNotificationMaskRequest).isNotNull
    }

    private fun DeviceRequestMessage.verifySetLightPayload() {
        assertThat(hasSetLightRequest()).isTrue
        assertThat(setLightRequest).isNotNull
    }

    private fun DeviceRequestMessage.verifySetSchedulePayload() {
        assertThat(hasSetScheduleRequest()).isTrue
        assertThat(setScheduleRequest).isNotNull
    }

    private fun DeviceRequestMessage.verifySetTransitionPayload() {
        assertThat(hasSetTransitionRequest()).isTrue
        assertThat(setTransitionRequest).isNotNull
    }
}
