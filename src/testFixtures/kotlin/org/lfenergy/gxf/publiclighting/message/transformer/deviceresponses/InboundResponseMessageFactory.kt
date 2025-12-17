// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.Result
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.deviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.getStatusResponse
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.responseHeader
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants

object InboundResponseMessageFactory {
    fun protobufMessageForResponseOfType(responseType: ResponseType): DeviceResponseMessage =
        when (responseType) {
            ResponseType.UNRECOGNIZED -> unrecognizedResponseMessage()
            ResponseType.GET_CONFIGURATION_RESPONSE -> getConfigurationResponseMessage()
            ResponseType.GET_FIRMWARE_VERSION_RESPONSE -> getFirmwareVersionResponseMessage()
            ResponseType.GET_STATUS_RESPONSE -> getStatusResponseMessage()
            else -> emptyResponseMessage(responseType)
        }

    private fun emptyResponseMessage(inboundResponseType: ResponseType) =
        deviceResponseMessage {
            header = responseHeader(inboundResponseType)
            result = Result.OK
        }

    private fun getConfigurationResponseMessage() =
        deviceResponseMessage {
            header = responseHeader(ResponseType.GET_CONFIGURATION_RESPONSE)
            result = Result.OK
            getConfigurationResponse = InboundResponsePayloadFactory.configurationPayload()
        }

    private fun getFirmwareVersionResponseMessage() =
        deviceResponseMessage {
            header = responseHeader(ResponseType.GET_FIRMWARE_VERSION_RESPONSE)
            result = Result.OK
            getFirmwareVersionResponse = InboundResponsePayloadFactory.firmwareVersionsPayload()
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
                    deviceIdentification = TestConstants.DEVICE_IDENTIFICATION
                    correlationUid = TestConstants.CORRELATION_UID
                    responseTypeValue = TestConstants.UNRECOGNIZED_VALUE
                }
        }

    private fun responseHeader(inboundResponseType: ResponseType) =
        responseHeader {
            deviceIdentification = TestConstants.DEVICE_IDENTIFICATION
            correlationUid = TestConstants.CORRELATION_UID
            responseType = inboundResponseType
            domain = TestConstants.DOMAIN
            domainVersion = TestConstants.DOMAIN_VERSION
        }
}
