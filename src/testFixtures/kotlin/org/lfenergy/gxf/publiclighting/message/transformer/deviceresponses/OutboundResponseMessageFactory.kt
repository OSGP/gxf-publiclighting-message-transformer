// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses

import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType
import java.io.Serializable

object OutboundResponseMessageFactory {
    fun responseMessageForType(messageType: ObjectMessageType): ProtocolResponseMessage =
        ProtocolResponseMessage(
            domain = TestConstants.DOMAIN,
            domainVersion = TestConstants.DOMAIN_VERSION,
            retryCount = TestConstants.RETRY_COUNT,
            deviceIdentification = TestConstants.DEVICE_IDENTIFICATION,
            organisationIdentification = TestConstants.ORGANIZATION_IDENTIFICATION,
            correlationUid = TestConstants.CORRELATION_UID,
            messageType = messageType.name,
            messagePriority = TestConstants.DEFAULT_PRIORITY,
            scheduled = false,
            maxScheduleTime = null,
            deviceModelCode = null,
            bypassRetry = true,
            retryHeader = null,
            result = ResponseMessageResultType.OK,
            osgpException = null,
            dataObject =
                when (messageType) {
                    ObjectMessageType.GET_CONFIGURATION -> OutboundResponsePayloadFactory.configurationPayload()
                    ObjectMessageType.GET_STATUS -> OutboundResponsePayloadFactory.statusPayload()
                    ObjectMessageType.GET_FIRMWARE_VERSION -> OutboundResponsePayloadFactory.firmwarePayload() as Serializable?
                    else -> null
                },
            topic = null,
        )
}
