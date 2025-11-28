// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.shared.infra.jms

import org.opensmartgridplatform.shared.exceptionhandling.OsgpException
import java.io.Serializable

class ProtocolResponseMessage(
    val domain: String?,
    val domainVersion: String?,
    val retryCount: Int,
    deviceIdentification: String?,
    organisationIdentification: String?,
    correlationUid: String?,
    messageType: String?,
    messagePriority: Int,
    scheduled: Boolean,
    maxScheduleTime: Long?,
    deviceModelCode: String?,
    bypassRetry: Boolean,
    retryHeader: RetryHeader?,
    result: ResponseMessageResultType?,
    osgpException: OsgpException?,
    dataObject: Serializable?,
    topic: String?,
) : ResponseMessage(
        deviceIdentification,
        organisationIdentification,
        correlationUid,
        messageType,
        messagePriority,
        scheduled,
        maxScheduleTime,
        deviceModelCode,
        bypassRetry,
        retryHeader,
        result,
        osgpException,
        dataObject,
        topic,
    ) {
    companion object {
        private const val serialVersionUID = -7720502773704936266L
    }
}
