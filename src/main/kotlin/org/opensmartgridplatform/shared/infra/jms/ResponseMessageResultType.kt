// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.opensmartgridplatform.shared.infra.jms

enum class ResponseMessageResultType(
    val value: String,
) {
    OK("OK"),
    NOT_FOUND("NOT FOUND"),
    NOT_OK("NOT OK"),
}
