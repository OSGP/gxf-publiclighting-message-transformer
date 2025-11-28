// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.deviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.requestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setScheduleRequest
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants

object DeviceRequestMessageFactory {
    fun deviceRequestMessage(requestType: RequestType) =
        when (requestType) {
            RequestType.GET_STATUS_REQUEST -> getStatusDeviceRequestMessage()
            RequestType.SET_LIGHT_REQUEST -> setLightDeviceRequestMessage()
            RequestType.SET_SCHEDULE_REQUEST -> setScheduleDeviceRequestMessage()
            else -> throw IllegalArgumentException("Unsupported request type: $requestType")
        }

    private fun getStatusDeviceRequestMessage() =
        deviceRequestMessage {
            header = requestHeader(RequestType.GET_STATUS_REQUEST)
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
            // TODO Implement schedule request payload creation
        }
}
