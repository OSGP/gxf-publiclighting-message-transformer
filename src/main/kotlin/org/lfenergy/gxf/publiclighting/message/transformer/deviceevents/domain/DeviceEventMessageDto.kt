// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.domain

import java.io.Serializable

data class DeviceEventMessageDto(
    val deviceIdentification: String,
    val correlationUid: String,
    val organisationIdentification: String,
    val messageType: String,
    val payload: Serializable,
)
