// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs

import com.google.protobuf.ByteString
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.Direction
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.logItemMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ENCODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ORGANIZATION_IDENTIFICATION

object InboundLogItemMessageFactory {
    fun protobufMessageForLogItem() =
        logItemMessage {
            deviceIdentification = DEVICE_IDENTIFICATION
            organizationIdentification = ORGANIZATION_IDENTIFICATION
            rawData = ByteString.copyFromUtf8(ENCODED_MESSAGE)
            decodedData = TestConstants.DECODED_MESSAGE
            direction = Direction.FROM_DEVICE
        }
}
