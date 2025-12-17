// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponsePayloadFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.OutboundResponsePayloadFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.GetFirmwareVersionResponseMapper.toDto

class GetFirmwareVersionResponseMapperTest {
    @Test
    fun `should convert get firmware version response to firmware versions dto`() {
        val firmwareVersions = InboundResponsePayloadFactory.firmwareVersionsPayload()
        val expectedFirmwareVersionsDto = OutboundResponsePayloadFactory.firmwarePayload()

        val firmwareVersionsDto = firmwareVersions.toDto()

        assertThat(firmwareVersionsDto).usingRecursiveComparison().isEqualTo(expectedFirmwareVersionsDto)
    }
}
