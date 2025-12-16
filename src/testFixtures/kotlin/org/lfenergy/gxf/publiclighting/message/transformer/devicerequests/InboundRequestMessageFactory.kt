// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ALL_RELAYS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ConfigurationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ConfigurationConstants.AstronomicalOffsetConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ConfigurationConstants.CommunicationConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ConfigurationConstants.DaylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ConfigurationConstants.DeviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ConfigurationConstants.PlatformAddressConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ELEVEN_PM
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.HALF_HOUR_IN_SECONDS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.RELAY_THREE
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.RELAY_TWO
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.SIX_AM
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.DeviceFixedIpDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.RelayConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.RelayMapDto
import org.opensmartgridplatform.dto.valueobjects.RelayMatrixDto
import org.opensmartgridplatform.dto.valueobjects.RelayTypeDto
import org.opensmartgridplatform.dto.valueobjects.ResumeScheduleMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleEntryDto
import org.opensmartgridplatform.dto.valueobjects.TransitionMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.TransitionTypeDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage

object InboundRequestMessageFactory {
    fun requestMessageForType(requestType: ObjectMessageType) =
        RequestMessage(
            CORRELATION_UID,
            ORGANIZATION_IDENTIFICATION,
            DEVICE_IDENTIFICATION,
            NETWORK_ADDRESS,
            null,
            null,
            when (requestType) {
                ObjectMessageType.SET_CONFIGURATION -> setConfigurationRequestPayload()
                ObjectMessageType.RESUME_SCHEDULE -> setResumeScheduleRequestPayload()
                ObjectMessageType.SET_EVENT_NOTIFICATIONS -> setEventNotificationsRequestPayload()
                ObjectMessageType.SET_LIGHT -> setLightRequestPayload()
                ObjectMessageType.SET_SCHEDULE -> setScheduleRequestPayload()
                ObjectMessageType.SET_TRANSITION -> setTransitionRequestPayload()
                else -> null // no payload for other types
            },
        )

    fun setConfigurationRequestPayload() =
        ConfigurationDto(
            astroGateSunRiseOffset = AstronomicalOffsetConfiguration.SUNRISE_OFFSET_IN_SECONDS,
            astroGateSunSetOffset = AstronomicalOffsetConfiguration.SUNSET_OFFSET_IN_SECONDS,
            automaticSummerTimingEnabled = DaylightSavingsTimeConfiguration.AUTO_ENABLED,
            communicationNumberOfRetries = CommunicationConfiguration.NUMBER_OF_RETRIES,
            communicationPauseTimeBetweenConnectionTrials = CommunicationConfiguration.DELAY_BETWEEN_CONNECTION_ATTEMPTS_IN_SECONDS,
            communicationTimeout = CommunicationConfiguration.CONNECTION_TIMEOUT_IN_SECONDS,
            deviceFixedIp =
                DeviceFixedIpDto(
                    DeviceAddressConfiguration.IP_ADDRESS,
                    DeviceAddressConfiguration.NET_MASK,
                    DeviceAddressConfiguration.GATEWAY,
                ),
            dhcpEnabled = DeviceAddressConfiguration.DHCP_ENABLED,
            lightType = ConfigurationConstants.LIGHT_TYPE_DTO_RELAY,
            osgpIpAddress = PlatformAddressConfiguration.IP_ADDRESS,
            osgpPortNumber = PlatformAddressConfiguration.PORT,
            preferredLinkType = CommunicationConfiguration.LINK_TYPE_DTO,
            relayConfiguration =
                RelayConfigurationDto(
                    mutableListOf(
                        RelayMapDto(2, 1, RelayTypeDto.LIGHT),
                        RelayMapDto(3, 2, RelayTypeDto.LIGHT),
                        RelayMapDto(4, 3, RelayTypeDto.LIGHT),
                    ),
                ),
            relayLinking =
                mutableListOf(
                    RelayMatrixDto(3, true, mutableListOf(4)),
                    RelayMatrixDto(3, false, mutableListOf(4)),
                ),
            relayRefreshing = ConfigurationConstants.RelayConfiguration.RELAY_REFRESHING_ENABLED,
            summerTimeDetails = DaylightSavingsTimeConfiguration.BEGIN_OF_DAYLIGHT_SAVINGS_TIME_DTO,
            switchingDelays = ConfigurationConstants.switchingDelays,
            timeSyncFrequency = 600,
            testButtonEnabled = ConfigurationConstants.TEST_BUTTON_ENABLED,
            winterTimeDetails = DaylightSavingsTimeConfiguration.END_OF_DAYLIGHT_SAVINGS_TIME_DTO,
            // TODO - Check unused fields
            commonNameString = null,
            daliConfiguration = null,
            ntpEnabled = null, // true,
            ntpHost = null, // "ntp-server",
            ntpSyncInterval = null, // 300,
            tlsEnabled = null, // true,
            tlsPortNumber = null, // 22125,
        )

    fun setEventNotificationsRequestPayload() =
        EventNotificationMessageDataContainerDto(
            listOf(
                EventNotificationTypeDto.DIAG_EVENTS,
                EventNotificationTypeDto.LIGHT_EVENTS,
                EventNotificationTypeDto.SECURITY_EVENTS,
            ),
        )

    fun setLightRequestPayload() =
        LightValueMessageDataContainerDto(
            listOf(
                LightValueDto(RELAY_TWO, true, null),
                LightValueDto(RELAY_THREE, true, null),
            ),
        )

    fun setResumeScheduleRequestPayload() =
        ResumeScheduleMessageDataContainerDto(
            null,
            true,
        )

    fun setScheduleRequestPayload() =
        ScheduleDto(
            null,
            null,
            mutableListOf(
                allLightsOnAtSunset(),
                allLightsOffAtSunrise(),
                eveningBurnerOffAt2300(),
                morningBurnerOnAt0600(),
            ),
        )

    fun setTransitionRequestPayload() =
        TransitionMessageDataContainerDto(
            TransitionTypeDto.DAY_NIGHT,
            null,
        )

    fun allLightsOnAtSunset() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.SUNSET,
            null,
            null,
            mutableListOf(LightValueDto(ALL_RELAYS, true, null)),
            TriggerTypeDto.ASTRONOMICAL,
            null,
            null,
            null,
        )

    fun allLightsOffAtSunrise() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.SUNRISE,
            null,
            null,
            mutableListOf(LightValueDto(ALL_RELAYS, false, null)),
            TriggerTypeDto.ASTRONOMICAL,
            null,
            null,
            null,
        )

    fun eveningBurnerOffAt2300() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.ABSOLUTETIME,
            ELEVEN_PM,
            null,
            mutableListOf(LightValueDto(RELAY_THREE, false, null)),
            null,
            null,
            null,
            HALF_HOUR_IN_SECONDS,
        )

    fun morningBurnerOnAt0600() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.ABSOLUTETIME,
            SIX_AM,
            null,
            mutableListOf(LightValueDto(RELAY_THREE, true, null)),
            null,
            null,
            null,
            HALF_HOUR_IN_SECONDS,
        )
}
