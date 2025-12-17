// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.RelayType
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.astronomicalOffsetsConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.communicationConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.daylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.deviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.platformAddressConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayConfiguration
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayLinkMatrix
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayLinking
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayMap
import org.lfenergy.gxf.publiclighting.contracts.internal.configuration.relayMapping
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ActionTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.NotificationType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TransitionType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TriggerType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.Weekday
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.deviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.requestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.resumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.scheduleEntry
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setConfigurationRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setEventNotificationMaskRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setTransitionRequest
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants.AstronomicalOffsetConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants.CommunicationConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants.DaylightSavingsTimeConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants.DeviceAddressConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants.PlatformAddressConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.common.ConfigurationConstants.RelayConfiguration
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ELEVEN_PM
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.HALF_HOUR_IN_SECONDS
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.SIX_AM
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.CommonMappingFunctions.toByteString

object OutboundRequestMessageFactory {
    fun deviceRequestMessage(requestType: RequestType) =
        deviceRequestMessage {
            header = requestHeader(requestType)
            when (requestType) {
                RequestType.SET_CONFIGURATION_REQUEST -> setConfigurationRequest()
                RequestType.RESUME_SCHEDULE_REQUEST -> resumeScheduleRequest()
                RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST -> setEventNotificationMaskRequest()
                RequestType.SET_LIGHT_REQUEST -> setLightRequest()
                RequestType.SET_SCHEDULE_REQUEST -> setScheduleRequest()
                RequestType.SET_TRANSITION_REQUEST -> setTransitionRequest()
                else -> {} // no payload
            }
        }

    private fun requestHeader(type: RequestType) =
        requestHeader {
            deviceIdentification = TestConstants.DEVICE_IDENTIFICATION
            correlationUid = TestConstants.CORRELATION_UID
            organizationIdentification = TestConstants.ORGANIZATION_IDENTIFICATION
            networkAddress = TestConstants.NETWORK_ADDRESS
            domain = TestConstants.DOMAIN
            domainVersion = TestConstants.DOMAIN_VERSION
            requestType = type
        }

    private fun setConfigurationRequest() {
        setConfigurationRequest {
            astronomicalOffsetsConfiguration {
                sunriseOffset = AstronomicalOffsetConfiguration.SUNRISE_OFFSET_IN_SECONDS
                sunsetOffset = AstronomicalOffsetConfiguration.SUNSET_OFFSET_IN_SECONDS
            }
            communicationConfiguration {
                preferredLinkType = CommunicationConfiguration.LINK_TYPE
                connectionTimeout = CommunicationConfiguration.CONNECTION_TIMEOUT_IN_SECONDS
                delayBetweenConnectionAttempts = CommunicationConfiguration.DELAY_BETWEEN_CONNECTION_ATTEMPTS_IN_SECONDS
                numberOfRetries = CommunicationConfiguration.NUMBER_OF_RETRIES
            }
            daylightSavingsTimeConfiguration {
                automaticSummerTimingEnabled = DaylightSavingsTimeConfiguration.AUTO_ENABLED
                summerTimeDetails = DaylightSavingsTimeConfiguration.BEGIN_OF_DAYLIGHT_SAVINGS_TIME
                winterTimeDetails = DaylightSavingsTimeConfiguration.END_OF_DAYLIGHT_SAVINGS_TIME
            }
            deviceAddressConfiguration {
                ipAddress = DeviceAddressConfiguration.IP_ADDRESS_BYTES
                netMask = DeviceAddressConfiguration.NET_MASK_BYTES
                gateway = DeviceAddressConfiguration.GATEWAY_BYTES
                dhcpEnabled = DeviceAddressConfiguration.DHCP_ENABLED
            }
            platformAddressConfiguration {
                ipAddress = PlatformAddressConfiguration.IP_ADDRESS_BYTES
                portNumber = PlatformAddressConfiguration.PORT
            }
            relayConfiguration {
                relayRefreshingEnabled = RelayConfiguration.RELAY_REFRESHING_ENABLED
                relayMapping {
                    relayMap.addAll(
                        listOf(
                            relayMap {
                                index = 2.toByteString()
                                address = 1.toByteString()
                                relayType = RelayType.LIGHT
                            },
                            relayMap {
                                index = 3.toByteString()
                                address = 2.toByteString()
                                relayType = RelayType.LIGHT
                            },
                            relayMap {
                                index = 4.toByteString()
                                address = 3.toByteString()
                                relayType = RelayType.LIGHT
                            },
                        ),
                    )
                }
                relayLinking {
                    relayLinkMatrix.addAll(
                        listOf(
                            relayLinkMatrix {
                                masterRelayIndex = 3.toByteString()
                                masterRelayOn = true
                                indicesOfControlledRelaysOn = 4.toByteString()
                            },
                            relayLinkMatrix {
                                masterRelayIndex = 3.toByteString()
                                masterRelayOn = false
                                indicesOfControlledRelaysOff = 4.toByteString()
                            },
                        ),
                    )
                }
            }
        }
    }

    private fun resumeScheduleRequest() =
        resumeScheduleRequest {
            immediate = true
        }

    private fun setEventNotificationMaskRequest() =
        setEventNotificationMaskRequest {
            notificationTypes.addAll(
                listOf(
                    NotificationType.DIAG_EVENTS,
                    NotificationType.LIGHT_EVENTS,
                    NotificationType.SECURITY_EVENTS,
                ),
            )
        }

    private fun setTransitionRequest() =
        setTransitionRequest {
            transitionType = TransitionType.SUNSET
        }

    private fun setLightRequest() =
        setLightRequest {
            lightValues.addAll(
                listOf(
                    lightValue {
                        index = RelayIndex.RELAY_TWO
                        lightOn = true
                    },
                    lightValue {
                        index = RelayIndex.RELAY_THREE
                        lightOn = true
                    },
                ),
            )
        }

    private fun setScheduleRequest() =
        setScheduleRequest {
            scheduleEntries.addAll(
                listOf(
                    scheduleEntry {
                        weekday = Weekday.ALL_DAYS
                        actionTime = ActionTime.SUNSET_TIME
                        triggerType = TriggerType.ASTRONOMICAL
                        lightValue {
                            index = RelayIndex.ALL_RELAYS
                            lightOn = true
                        }
                    },
                    scheduleEntry {
                        weekday = Weekday.ALL_DAYS
                        actionTime = ActionTime.SUNRISE_TIME
                        triggerType = TriggerType.ASTRONOMICAL
                        lightValue {
                            index = RelayIndex.ALL_RELAYS
                            lightOn = false
                        }
                    },
                    scheduleEntry {
                        weekday = Weekday.ALL_DAYS
                        actionTime = ActionTime.ABSOLUTE_TIME
                        time = ELEVEN_PM
                        minimumLightsOn = HALF_HOUR_IN_SECONDS
                        lightValue {
                            index = RelayIndex.RELAY_THREE
                            lightOn = false
                        }
                    },
                    scheduleEntry {
                        weekday = Weekday.ALL_DAYS
                        actionTime = ActionTime.ABSOLUTE_TIME
                        time = SIX_AM
                        minimumLightsOn = 1800
                        lightValue {
                            index = RelayIndex.ALL_RELAYS
                            lightOn = true
                        }
                    },
                ),
            )
        }
}
