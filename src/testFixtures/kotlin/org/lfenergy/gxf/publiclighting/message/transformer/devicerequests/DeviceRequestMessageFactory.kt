// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ActionTime
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
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setTransitionRequest
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ELEVEN_PM
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.HALF_HOUR_IN_SECONDS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.SIX_AM

object DeviceRequestMessageFactory {
    fun deviceRequestMessage(requestType: RequestType) =
        when (requestType) {
            RequestType.GET_STATUS_REQUEST -> emptyDeviceRequestMessage(RequestType.GET_STATUS_REQUEST)
            RequestType.REBOOT_REQUEST -> emptyDeviceRequestMessage(RequestType.REBOOT_REQUEST)
            RequestType.RESUME_SCHEDULE_REQUEST -> resumeScheduleDeviceRequestMessage()
            RequestType.SET_LIGHT_REQUEST -> setLightDeviceRequestMessage()
            RequestType.SET_SCHEDULE_REQUEST -> setScheduleDeviceRequestMessage()
            RequestType.SET_TRANSITION_REQUEST -> setTransitionDeviceRequestMessage()
            RequestType.START_SELF_TEST_REQUEST -> emptyDeviceRequestMessage(RequestType.START_SELF_TEST_REQUEST)
            RequestType.STOP_SELF_TEST_REQUEST -> emptyDeviceRequestMessage(RequestType.STOP_SELF_TEST_REQUEST)
            else -> throw IllegalArgumentException("Unsupported request type: $requestType")
        }

    private fun emptyDeviceRequestMessage(requestType: RequestType) =
        deviceRequestMessage {
            header = requestHeader(requestType)
        }

    private fun resumeScheduleDeviceRequestMessage() =
        deviceRequestMessage {
            header = requestHeader(RequestType.RESUME_SCHEDULE_REQUEST)
            resumeScheduleRequest =
                resumeScheduleRequest {
                    immediate = true
                }
        }

    private fun setLightDeviceRequestMessage() =
        deviceRequestMessage {
            header = requestHeader(RequestType.SET_LIGHT_REQUEST)
            setLightRequest = setLightRequest()
        }

    private fun setScheduleDeviceRequestMessage() =
        deviceRequestMessage {
            header = requestHeader(RequestType.SET_SCHEDULE_REQUEST)
            setScheduleRequest = setScheduleRequest()
        }

    private fun setTransitionDeviceRequestMessage() =
        deviceRequestMessage {
            header = requestHeader(RequestType.SET_TRANSITION_REQUEST)
            setTransitionRequest =
                setTransitionRequest {
                    transitionType = TransitionType.SUNSET
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
