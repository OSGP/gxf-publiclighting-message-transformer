// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper

import com.google.protobuf.ByteString
import io.github.oshai.kotlinlogging.KotlinLogging
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.Direction
import java.util.Base64

object LogItemMessageMapper {
    private val logger = KotlinLogging.logger { }

    fun ByteString.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray())

    fun Direction.toIsIncoming() =
        when (this) {
            Direction.FROM_DEVICE -> "true"
            Direction.TO_DEVICE -> "false"
            else -> {
                logger.warn { "Unsupported direction: $this" }
                null
            }
        }
}
