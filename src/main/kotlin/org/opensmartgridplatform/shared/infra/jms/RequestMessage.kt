// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.shared.infra.jms

import java.io.Serializable

data class RequestMessage(
    val correlationUid: String? = null,
    val organisationIdentification: String? = null,
    val deviceIdentification: String? = null,
    val ipAddress: String? = null,
    val baseTransceiverStationId: Int? = null,
    val cellId: Int? = null,
    val request: Serializable? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 8377090502244471258L
    }
}
