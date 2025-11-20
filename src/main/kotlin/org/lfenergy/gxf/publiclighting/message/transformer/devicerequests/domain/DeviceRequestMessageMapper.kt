// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain

import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.deviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.requestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageType.SET_LIGHT_REQUEST
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain.DeviceRequestMessageType.SET_SCHEDULE_REQUEST
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto

object DeviceRequestMessageMapper {
    fun DeviceRequestMessageDto.toProtobufMessage(): DeviceRequestMessage =
        deviceRequestMessage {
            requestHeader {
                deviceIdentification = this@toProtobufMessage.deviceIdentification
                correlationUid = this@toProtobufMessage.correlationUid
                organizationIdentification = this@toProtobufMessage.organizationIdentification
                requestType = this@toProtobufMessage.messageType.toProtobufRequestType()
            }
            when (messageType) {
                SET_LIGHT_REQUEST -> (payload as LightValueMessageDataContainerDto).toProtobufMessage()
                else -> throw IllegalArgumentException("Unsupported message type: $messageType")
            }
        }

    fun DeviceRequestMessageType.toProtobufRequestType() =
        when (this) {
            SET_LIGHT_REQUEST -> RequestType.SET_LIGHT_REQUEST
            SET_SCHEDULE_REQUEST -> RequestType.SET_SCHEDULE_REQUEST
//            else -> throw IllegalArgumentException("Unsupported message type: $this")
        }

    fun LightValueMessageDataContainerDto.toProtobufMessage() =
        setLightRequest {
            lightValues.addAll(
                this@toProtobufMessage.lightValues.map { lightValueDto ->
                    lightValue {
                        index = lightValueDto.index!!.toRelayIndex()
                        lightOn = lightValueDto.on
                    }
                },
            )
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
