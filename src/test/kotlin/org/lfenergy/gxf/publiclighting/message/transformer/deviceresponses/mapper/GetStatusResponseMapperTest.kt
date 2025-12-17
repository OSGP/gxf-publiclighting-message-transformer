// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponsePayloadFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.OutboundResponsePayloadFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.GetStatusResponseMapper.toDto

class GetStatusResponseMapperTest {
    @Test
    fun `should convert get status response to status dto`() {
        val status = InboundResponsePayloadFactory.statusPayload()
        val expectedStatusDto = OutboundResponsePayloadFactory.statusPayload()

        val statusDto = status.toDto()

        assertThat(statusDto).usingRecursiveComparison().isEqualTo(expectedStatusDto)
    }
}
