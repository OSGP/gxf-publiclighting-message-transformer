// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.Result
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.deviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.responseHeader
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.UNRECOGNIZED_VALUE

object DeviceResponseMessageFactory {
    fun protobufMessageForResponseOfType(responseType: ResponseType): DeviceResponseMessage =
        when (responseType) {
            ResponseType.GET_STATUS_RESPONSE -> getStatusResponseMessage()
            ResponseType.SET_LIGHT_RESPONSE -> setLightResponseMessage()
            ResponseType.SET_SCHEDULE_RESPONSE -> setScheduleResponseMessage()
            else -> unrecognizedResponseMessage()
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
            // Add payload fields as needed
        }

    private fun setLightResponseMessage() =
        deviceResponseMessage {
            header =
                responseHeader {
                    deviceIdentification = DEVICE_IDENTIFICATION
                    correlationUid = CORRELATION_UID
                    responseType = ResponseType.SET_LIGHT_RESPONSE
                }
            result = Result.OK
        }

    private fun setScheduleResponseMessage() =
        deviceResponseMessage {
            header =
                responseHeader {
                    deviceIdentification = DEVICE_IDENTIFICATION
                    correlationUid = CORRELATION_UID
                    responseType = ResponseType.SET_SCHEDULE_RESPONSE
                }
            result = Result.OK
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
