// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.FirmwareType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.Result
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.deviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.firmwareVersion
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.getFirmwareVersionResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.getStatusResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.responseHeader
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.UNRECOGNIZED_VALUE

object DeviceResponseMessageFactory {
    fun protobufMessageForResponseOfType(responseType: ResponseType): DeviceResponseMessage =
        when (responseType) {
            ResponseType.UNRECOGNIZED -> unrecognizedResponseMessage()
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> getFirmwareVersionResponseMessage()
            ResponseType.GET_STATUS_RESPONSE -> getStatusResponseMessage()
            else -> emptyResponseMessage(responseType)
        }

    private fun emptyResponseMessage(inboundResponseType: ResponseType) =
        deviceResponseMessage {
            header = responseHeader(inboundResponseType)
            result = Result.OK
        }

    private fun getFirmwareVersionResponseMessage() =
        deviceResponseMessage {
            header = responseHeader(ResponseType.GET_FIRMWARE_VERSION_RESPONSE)
            result = Result.OK
            getFirmwareVersionResponse =
                getFirmwareVersionResponse {
                    firmwareVersions.add(
                        firmwareVersion {
                            firmwareType = FirmwareType.FUNCTIONAL
                            version = "0.9.0"
                        },
                    )
                }
        }

    private fun getStatusResponseMessage() =
        deviceResponseMessage {
            header = responseHeader(ResponseType.GET_STATUS_RESPONSE)
            result = Result.OK
            getStatusResponse =
                getStatusResponse {
                    currentIp = ""
                    // TODO Add payload fields
                }
        }

    private fun unrecognizedResponseMessage() =
        deviceResponseMessage {
            header =
                responseHeader {
                    deviceIdentification = DEVICE_IDENTIFICATION
                    correlationUid = CORRELATION_UID
                    responseTypeValue = UNRECOGNIZED_VALUE
                }
        }

    private fun responseHeader(inboundResponseType: ResponseType) =
        responseHeader {
            deviceIdentification = DEVICE_IDENTIFICATION
            correlationUid = CORRELATION_UID
            responseType = inboundResponseType
            domain = "PUBLIC_LIGHTING"
            domainVersion = "1.0"
        }
}
