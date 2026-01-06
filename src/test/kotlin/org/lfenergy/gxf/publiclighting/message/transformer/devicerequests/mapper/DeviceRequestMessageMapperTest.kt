// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.AstronomicalOffsetsConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.CommunicationConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.Configuration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.DaylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.DeviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.PlatformAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ResumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetScheduleRequest
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.DeviceRequestMessageMapper.toProtobufMessage
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestObjectMessageMockFactory as MockFactory

class DeviceRequestMessageMapperTest {
    @Test
    fun `should map get configuration request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.GET_CONFIGURATION, RequestType.GET_CONFIGURATION_REQUEST)

    @Test
    fun `should map get firmware version request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.GET_FIRMWARE_VERSION, RequestType.GET_FIRMWARE_VERSION_REQUEST)

    @Test
    fun `should map get status request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.GET_STATUS, RequestType.GET_STATUS_REQUEST)

    @Test
    fun `should map reboot device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_REBOOT, RequestType.REBOOT_REQUEST)

    @Test
    fun `should map resume schedule device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.RESUME_SCHEDULE, RequestType.RESUME_SCHEDULE_REQUEST)

    @Test
    fun `should map set configuration device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_CONFIGURATION, RequestType.SET_CONFIGURATION_REQUEST)

    @Test
    fun `should map set event notification mask device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_EVENT_NOTIFICATIONS, RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST)

    @Test
    fun `should map set light device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_LIGHT, RequestType.SET_LIGHT_REQUEST)

    @Test
    fun `should map set schedule device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_SCHEDULE, RequestType.SET_SCHEDULE_REQUEST)

    @Test
    fun `should map set transition device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.SET_TRANSITION, RequestType.SET_TRANSITION_REQUEST)

    @Test
    fun `should map start self test device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.START_SELF_TEST, RequestType.START_SELF_TEST_REQUEST)

    @Test
    fun `should map stop self test device request object message to protobuf message`() =
        testDeviceRequestMapping(ObjectMessageType.STOP_SELF_TEST, RequestType.STOP_SELF_TEST_REQUEST)

    private fun testDeviceRequestMapping(
        inboundMessageType: ObjectMessageType,
        expectedRequestType: RequestType,
    ) {
        val message = MockFactory.deviceRequestObjectMessageMock(inboundMessageType)

        val result = message.toProtobufMessage()

        assertThat(result).isInstanceOf(DeviceRequestMessage::class.java)
        assertThat(result.header.requestType).isEqualTo(expectedRequestType)

        when (inboundMessageType) {
            ObjectMessageType.RESUME_SCHEDULE -> verifyResumeSchedulePayload(result)
            ObjectMessageType.SET_CONFIGURATION -> verifySetConfigurationPayload(result)
            ObjectMessageType.SET_LIGHT -> verifySetLightPayload(result)
            ObjectMessageType.SET_SCHEDULE -> verifySetSchedulePayload(result)
            ObjectMessageType.SET_TRANSITION -> verifySetTransitionPayload(result)
            ObjectMessageType.SET_EVENT_NOTIFICATIONS -> verifySetEventNotificationPayload(result)
            else -> verifyNoPayload(result)
        }
    }

    private fun verifyNoPayload(deviceRequestMessage: DeviceRequestMessage) {
        assertThat(deviceRequestMessage.hasResumeScheduleRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetScheduleRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetLightRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetTransitionRequest()).isFalse
        assertThat(deviceRequestMessage.hasSetEventNotificationMaskRequest()).isFalse
    }

    fun verifyResumeSchedulePayload(message: DeviceRequestMessage) {
        assertThat(message.hasResumeScheduleRequest()).isTrue
        assertThat(message.resumeScheduleRequest).isInstanceOf(ResumeScheduleRequest::class.java)
    }

    private fun verifySetConfigurationPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetConfigurationRequest()).isTrue
        assertThat(message.setConfigurationRequest).isNotNull
        assertThat(message.setConfigurationRequest.configuration).isNotNull
        verifyConfigurationPayload(message.setConfigurationRequest.configuration)
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

    private fun verifySetEventNotificationPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetEventNotificationMaskRequest()).isTrue
        assertThat(message.setEventNotificationMaskRequest).isNotNull
    }

    private fun verifySetLightPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetLightRequest()).isTrue
        assertThat(message.setLightRequest).isInstanceOf(SetLightRequest::class.java)
    }

    private fun verifySetSchedulePayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetScheduleRequest()).isTrue
        assertThat(message.setScheduleRequest).isInstanceOf(SetScheduleRequest::class.java)
    }

    private fun verifySetTransitionPayload(message: DeviceRequestMessage) {
        assertThat(message.hasSetTransitionRequest()).isTrue
        assertThat(message.setTransitionRequest).isNotNull
    }
}
