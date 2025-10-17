// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.modulith.Modulith

@SpringBootApplication
@Modulith
class GxfPubliclightingMessageTransformerApplication

fun main(args: Array<String>) {
    runApplication<GxfPubliclightingMessageTransformerApplication>(*args)
}
