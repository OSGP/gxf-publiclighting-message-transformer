// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.deviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.requestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto

object DeviceRequestMessageMapper {
    fun ObjectMessage.toProtobufMessage(): DeviceRequestMessage {
        val message = this
        val messageType = message.jmsType.toProtobufRequestType()

        return deviceRequestMessage {
            header =
                requestHeader {
                    deviceIdentification = message.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
                    correlationUid = message.jmsCorrelationID
                    organizationIdentification = message.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION)
                    networkAddress = message.getStringProperty(JMS_PROPERTY_NETWORK_ADDRESS)
                    requestType = messageType
                }
            when (messageType) {
                RequestType.GET_STATUS_REQUEST -> {} // No payload for get status request
                RequestType.SET_LIGHT_REQUEST -> setLightRequest = (`object` as LightValueMessageDataContainerDto).toProtobufMessage()
                else -> throw IllegalArgumentException("Unsupported message type: $jmsType")
            }
        }
    }

    fun String.toProtobufRequestType() =
        when (this) {
            "GET_STATUS" -> RequestType.GET_STATUS_REQUEST
            "SET_LIGHT" -> RequestType.SET_LIGHT_REQUEST
            "SET_SCHEDULE" -> RequestType.SET_SCHEDULE_REQUEST
            else -> throw IllegalArgumentException("Unsupported message type: $this")
        }

    fun LightValueMessageDataContainerDto.toProtobufMessage(): SetLightRequest {
        val dto = this
        return setLightRequest {
            lightValues.addAll(
                dto.lightValues.map { lightValueDto ->
                    lightValue {
                        index = lightValueDto.index!!.toRelayIndex()
                        lightOn = lightValueDto.on
                    }
                },
            )
        }
    }

    fun Int.toRelayIndex() =
        when (this) {
            0 -> RelayIndex.RELAY_ALL
            1 -> RelayIndex.RELAY_ONE
            2 -> RelayIndex.RELAY_TWO
            3 -> RelayIndex.RELAY_THREE
            4 -> RelayIndex.RELAY_FOUR
            else -> throw IllegalArgumentException("Unsupported relay index: $this")
        }
}
