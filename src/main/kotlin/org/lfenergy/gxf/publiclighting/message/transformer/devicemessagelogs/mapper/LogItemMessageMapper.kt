// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper

import io.github.oshai.kotlinlogging.KotlinLogging
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.Direction

object LogItemMessageMapper {
    private val logger = KotlinLogging.logger { }

    fun Direction.toIsIncoming() =
        when (this) {
            Direction.FROM_DEVICE -> true.toString()
            Direction.TO_DEVICE -> false.toString()
            else -> {
                logger.warn { "Unsupported message type: $this" }
                null
            }
        }
}
