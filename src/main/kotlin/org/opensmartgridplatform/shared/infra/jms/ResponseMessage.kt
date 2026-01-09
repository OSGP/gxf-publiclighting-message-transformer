// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.shared.infra.jms

import org.opensmartgridplatform.shared.exceptionhandling.OsgpException
import java.io.Serializable

open class ResponseMessage(
    val deviceIdentification: String? = null,
    val organisationIdentification: String? = null,
    val correlationUid: String? = null,
    val messageType: String? = null,
    val messagePriority: Int = DEFAULT_MESSAGE_PRIORITY,
    val scheduled: Boolean = false,
    val maxScheduleTime: Long? = null,
    val deviceModelCode: String? = null,
    val bypassRetry: Boolean = DEFAULT_BYPASS_RETRY,
    val retryHeader: RetryHeader? = null,
    val result: ResponseMessageResultType? = null,
    val osgpException: OsgpException? = null,
    val dataObject: Serializable? = null,
    val topic: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = -214808702310700742L

        private const val DEFAULT_BYPASS_RETRY = false
        private const val DEFAULT_MESSAGE_PRIORITY = 4
    }
}
