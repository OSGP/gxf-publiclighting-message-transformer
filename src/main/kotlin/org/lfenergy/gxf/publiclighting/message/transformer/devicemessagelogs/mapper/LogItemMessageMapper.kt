// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper

import com.google.protobuf.ByteString
import org.lfenergy.gxf.publiclighting.contracts.internal.audittrail.MessageType

object LogItemMessageMapper {
    private const val MAX_MESSAGE_LENGTH = 8000

    fun ByteString.toDecodedMessage(): String {
        // TODO implement
        return "decoded message"
    }

    fun MessageType.toIsIncoming() =
        when (this) {
            MessageType.FROM_DEVICE -> true.toString()
            MessageType.TO_DEVICE -> false.toString()
            else -> throw IllegalArgumentException("Illegal message type: $this")
        }
}
