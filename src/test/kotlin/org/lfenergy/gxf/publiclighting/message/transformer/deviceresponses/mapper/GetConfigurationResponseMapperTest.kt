// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponsePayloadFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.OutboundResponsePayloadFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.GetConfigurationResponseMapper.toDto
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto

class GetConfigurationResponseMapperTest {
    @Test
    fun `should convert get configuration response to configuration dto`() {
        val configuration = InboundResponsePayloadFactory.configurationPayload()
        val expectedConfigurationDto = OutboundResponsePayloadFactory.configurationPayload()

        val configurationDto: ConfigurationDto = configuration.toDto()

        assertThat(configurationDto).usingRecursiveComparison().isEqualTo(expectedConfigurationDto)
    }
}
