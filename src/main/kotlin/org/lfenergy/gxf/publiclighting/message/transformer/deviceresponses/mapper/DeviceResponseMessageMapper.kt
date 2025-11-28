// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.GetStatusResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.Result
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType
import java.io.Serializable
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage as ProtobufMessage

object DeviceResponseMessageMapper {
    fun ProtobufMessage.toResponseDto() =
        ProtocolResponseMessage(
            this.header.domain,
            this.header.domainVersion,
            0,
            this.header.deviceIdentification,
            this.header.organizationIdentification,
            this.header.correlationUid,
            this.header.responseType.name
                .removeSuffix("_RESPONSE"),
            this.header.priority,
            false,
            null,
            null,
            true,
            null,
            this.result.toProtocolResponseMessageResultType(),
            this.toOsgpException(),
            this.toSerializable(),
            null,
        )

    private fun Result.toProtocolResponseMessageResultType(): ResponseMessageResultType =
        when (this) {
            Result.OK -> ResponseMessageResultType.OK
            Result.NOT_OK -> ResponseMessageResultType.NOT_OK
            Result.UNRECOGNIZED -> ResponseMessageResultType.NOT_OK
        }

    private fun ProtobufMessage.toOsgpException() =
        if (this.hasErrorResponse()) {
            OsgpException(ComponentType.PROTOCOL_OSLP, errorResponse.errorMessage)
        } else {
            null
        }

    fun ProtobufMessage.toSerializable(): Serializable? {
        if (this.result == Result.NOT_OK) {
            return null
        }

        return when (this.header.responseType) {
            ResponseType.GET_STATUS_RESPONSE -> this.getStatusResponse.toDto()
            ResponseType.SET_LIGHT_RESPONSE -> null
            else -> throw IllegalArgumentException("Unsupported message type: ${this.header.responseType}")
        }
    }

    private fun GetStatusResponse.toDto(): DeviceStatusDto =
        DeviceStatusDto(
            this.lightValuesList.map { it -> LightValueDto(it.index.toInt(), it.lightOn, null) }.toMutableList(),
            this.preferredLinkType.toDto(),
            this.actualLinkType.toDto(),
            this.lightType.toDto(),
            this.eventNotificationMask,
            this.numberOfOutputs,
            this.dcOutputVoltageMaximum,
            this.dcOutputVoltageCurrent,
            this.maximumOutputPowerOnDcOutput,
            this.serialNumber.toStringUtf8(),
            this.macAddress.toStringUtf8(),
            this.hardwareId,
            this.internalFlashMemSize,
            this.externalFlashMemSize,
            this.lastInternalTestResultCode,
            this.startupCounter,
            this.bootLoaderVersion,
            this.firmwareVersion,
            this.currentConfigurationBackUsed.toStringUtf8(),
            this.name,
            this.currentTime,
            this.currentIp,
        )

    private fun RelayIndex.toInt() =
        when (this) {
            RelayIndex.RELAY_ALL -> 0
            RelayIndex.RELAY_ONE -> 1
            RelayIndex.RELAY_TWO -> 2
            RelayIndex.RELAY_THREE -> 3
            RelayIndex.RELAY_FOUR -> 4
            else -> throw IllegalArgumentException("Unsupported relay index: $this")
        }

    private fun LinkType.toDto() =
        when (this) {
            LinkType.ETHERNET -> LinkTypeDto.ETHERNET
            LinkType.CDMA -> LinkTypeDto.CDMA
            LinkType.GPRS -> LinkTypeDto.GPRS
            else -> throw IllegalArgumentException("Unsupported link type: $this")
        }

    private fun LightType.toDto() =
        when (this) {
            LightType.RELAY -> LightTypeDto.RELAY
            // Other enum values not used
            else -> throw IllegalArgumentException("Unsupported light type: $this")
        }
}
