// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.config

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.activemq.ArtemisContainer

@Configuration
class ArtemisContainerConfiguration {
    @Bean
    @ServiceConnection
    fun artemisContainer() = ArtemisContainer("apache/activemq-artemis:2.30.0-alpine")
}
