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
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.UNRECOGNIZED_VALUE

object DeviceResponseMessageFactory {
    fun protobufMessageForResponseOfType(responseType: ResponseType): DeviceResponseMessage =
        when (responseType) {
            ResponseType.GET_STATUS_RESPONSE -> getStatusResponseMessage()
            ResponseType.REBOOT_RESPONSE -> emptyResponseMessage(ResponseType.REBOOT_RESPONSE)
            ResponseType.START_SELF_TEST_RESPONSE -> emptyResponseMessage(ResponseType.START_SELF_TEST_RESPONSE)
            ResponseType.STOP_SELF_TEST_RESPONSE -> emptyResponseMessage(ResponseType.STOP_SELF_TEST_RESPONSE)
            ResponseType.SET_LIGHT_RESPONSE -> emptyResponseMessage(ResponseType.SET_LIGHT_RESPONSE)
            ResponseType.SET_SCHEDULE_RESPONSE -> emptyResponseMessage(ResponseType.SET_SCHEDULE_RESPONSE)
            else -> unrecognizedResponseMessage()
        }

    private fun emptyResponseMessage(inboundResponseType: ResponseType) =
        deviceResponseMessage {
            header =
                responseHeader {
                    deviceIdentification = DEVICE_IDENTIFICATION
                    correlationUid = CORRELATION_UID
                    responseType = inboundResponseType
                }
            result = Result.OK
        }

    private fun getStatusResponseMessage() =
        deviceResponseMessage {
            header =
                responseHeader {
                    deviceIdentification = DEVICE_IDENTIFICATION
                    correlationUid = CORRELATION_UID
                    responseType = ResponseType.GET_STATUS_RESPONSE
                }
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
}
