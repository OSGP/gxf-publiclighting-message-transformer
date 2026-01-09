// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.Result
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.GetConfigurationResponseMapper.toDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.GetFirmwareVersionResponseMapper.toDto
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.GetStatusResponseMapper.toDto
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
            this.header.responseType.toMessageType(),
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
            ResponseType.GET_CONFIGURATION_RESPONSE -> this.getConfigurationResponse.toDto()
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> this.getFirmwareVersionResponse.toDto()
            ResponseType.GET_STATUS_RESPONSE -> this.getStatusResponse.toDto()
            ResponseType.REBOOT_RESPONSE,
            ResponseType.RESUME_SCHEDULE_RESPONSE,
            ResponseType.SET_CONFIGURATION_RESPONSE,
            ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE,
            ResponseType.SET_LIGHT_RESPONSE,
            ResponseType.SET_SCHEDULE_RESPONSE,
            ResponseType.SET_TRANSITION_RESPONSE,
            ResponseType.START_SELF_TEST_RESPONSE,
            ResponseType.STOP_SELF_TEST_RESPONSE,
            -> null
            else -> throw IllegalArgumentException("Unsupported message type: ${this.header.responseType}")
        }
    }

    fun ResponseType.toMessageType() =
        when (this) {
            ResponseType.GET_CONFIGURATION_RESPONSE -> "GET_CONFIGURATION"
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> "GET_FIRMWARE_VERSION"
            ResponseType.GET_STATUS_RESPONSE -> "GET_STATUS"
            ResponseType.REBOOT_RESPONSE -> "SET_REBOOT"
            ResponseType.RESUME_SCHEDULE_RESPONSE -> "RESUME_SCHEDULE"
            ResponseType.SET_CONFIGURATION_RESPONSE -> "SET_CONFIGURATION"
            ResponseType.SET_EVENT_NOTIFICATION_MASK_RESPONSE -> "SET_EVENT_NOTIFICATIONS"
            ResponseType.SET_LIGHT_RESPONSE -> "SET_LIGHT"
            ResponseType.SET_SCHEDULE_RESPONSE -> "SET_SCHEDULE"
            ResponseType.SET_TRANSITION_RESPONSE -> "SET_TRANSITION"
            ResponseType.START_SELF_TEST_RESPONSE -> "START_SELF_TEST"
            ResponseType.STOP_SELF_TEST_RESPONSE -> "STOP_SELF_TEST"
            ResponseType.UNRECOGNIZED -> "UNRECOGNIZED"
        }
}
