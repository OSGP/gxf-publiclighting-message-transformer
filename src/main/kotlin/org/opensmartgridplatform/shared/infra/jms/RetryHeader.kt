// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.shared.infra.jms

import java.io.Serializable
import java.util.Date

class RetryHeader(
    private val retryCount: Int,
    private val maxRetries: Int,
    private val scheduledRetryTime: Date?,
) : Serializable {
    companion object {
        private const val serialVersionUID = 8503630227262375613L
    }
}
