// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.domain

import java.io.Serializable

data class DeviceRequestMessageDto(
    val deviceIdentification: String,
    val correlationUid: String,
    val organizationIdentification: String,
    val messageType: DeviceRequestMessageType,
    val payload: Serializable,
)
