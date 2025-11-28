// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.shared.exceptionhandling

import java.lang.Exception

class OsgpException(
    val componentType: ComponentType,
    message: String,
) : Exception(message) {
    companion object {
        private const val serialVersionUID = 3985910152334024442L
    }
}
