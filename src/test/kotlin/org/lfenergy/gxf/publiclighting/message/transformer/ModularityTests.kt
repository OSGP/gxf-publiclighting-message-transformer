// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

internal class ModularityTests {
    var modules: ApplicationModules = ApplicationModules.of(GxfPubliclightingMessageTransformerApplication::class.java)

    @Test
    fun verifiesModularStructure() {
        modules.verify()
    }

    @Test
    fun createModuleDocumentation() {
        Documenter(modules).writeDocumentation()
    }
}
