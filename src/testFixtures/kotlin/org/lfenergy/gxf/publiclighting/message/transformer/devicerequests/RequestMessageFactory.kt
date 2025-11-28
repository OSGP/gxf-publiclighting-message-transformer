// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ORGANIZATION_IDENTIFICATION
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage

object RequestMessageFactory {
    fun requestMessageForType(requestType: ObjectMessageType) =
        RequestMessage(
            CORRELATION_UID,
            ORGANIZATION_IDENTIFICATION,
            DEVICE_IDENTIFICATION,
            NETWORK_ADDRESS,
            null,
            null,
            when (requestType) {
                ObjectMessageType.GET_STATUS -> null
                ObjectMessageType.SET_LIGHT -> setLightRequestPayload()
//                ObjectMessageType.SET_SCHEDULE -> setScheduleRequestPayload()
                else -> null
            },
        )

    fun setLightRequestPayload() =
        LightValueMessageDataContainerDto(
            listOf(
                LightValueDto(2, true, null),
                LightValueDto(3, true, null),
            ),
        )

    fun setScheduleRequestPayload() = null // TODO Implement schedule request payload creation
}
