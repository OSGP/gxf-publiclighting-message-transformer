// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.FirmwareType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.GetFirmwareVersionResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.GetStatusResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LightType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.LinkType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.Result
import org.opensmartgridplatform.dto.valueobjects.DeviceStatusDto
import org.opensmartgridplatform.dto.valueobjects.FirmwareModuleType
import org.opensmartgridplatform.dto.valueobjects.FirmwareVersionDto
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
            this.header.responseType.toDto(),
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

    private fun ProtobufMessage.toSerializable(): Serializable? {
        if (this.result == Result.NOT_OK) {
            return null
        }

        return when (this.header.responseType) {
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> this.getFirmwareVersionResponse.toDto()
            ResponseType.GET_STATUS_RESPONSE -> this.getStatusResponse.toDto()
            ResponseType.REBOOT_RESPONSE -> null
            ResponseType.RESUME_SCHEDULE_RESPONSE -> null
            ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE -> null
            ResponseType.SET_LIGHT_RESPONSE -> null
            ResponseType.SET_SCHEDULE_RESPONSE -> null
            ResponseType.SET_TRANSITION_RESPONSE -> null
            ResponseType.START_SELF_TEST_RESPONSE -> null
            ResponseType.STOP_SELF_TEST_RESPONSE -> null
            else -> throw IllegalArgumentException("Unsupported message type: ${this.header.responseType}")
        }
    }

    private fun GetFirmwareVersionResponse.toDto(): Serializable =
        this.firmwareVersionsList.map {
            FirmwareVersionDto(
                it.firmwareType.toDto(),
                it.version,
            )
        } as Serializable

    private fun FirmwareType.toDto(): FirmwareModuleType =
        when (this) {
            FirmwareType.COMMUNICATION -> FirmwareModuleType.COMMUNICATION
            FirmwareType.FUNCTIONAL -> FirmwareModuleType.FUNCTIONAL
            FirmwareType.SECURITY -> FirmwareModuleType.SECURITY
            else -> throw IllegalArgumentException("Unsupported firmware type: $this")
        }

    private fun GetStatusResponse.toDto(): DeviceStatusDto =
        DeviceStatusDto(
            this.lightValuesList.map { LightValueDto(it.index.toInt(), it.lightOn, null) }.toMutableList(),
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

    fun ResponseType.toDto() =
        when (this) {
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> "GET_FIRMWARE_VERSION"
            ResponseType.GET_STATUS_RESPONSE -> "GET_STATUS"
            ResponseType.REBOOT_RESPONSE -> "SET_REBOOT"
            ResponseType.RESUME_SCHEDULE_RESPONSE -> "RESUME_SCHEDULE"
            ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE -> "SET_EVENT_NOTIFICATIONS"
            ResponseType.SET_LIGHT_RESPONSE -> "SET_LIGHT"
            ResponseType.SET_SCHEDULE_RESPONSE -> "SET_SCHEDULE"
            ResponseType.SET_TRANSITION_RESPONSE -> "SET_TRANSITION"
            ResponseType.START_SELF_TEST_RESPONSE -> "START_SELF_TEST"
            ResponseType.STOP_SELF_TEST_RESPONSE -> "STOP_SELF_TEST"
            ResponseType.UNRECOGNIZED -> "UNRECOGNIZED"
            else -> throw IllegalArgumentException("Unsupported response type: $this")
        }
}
