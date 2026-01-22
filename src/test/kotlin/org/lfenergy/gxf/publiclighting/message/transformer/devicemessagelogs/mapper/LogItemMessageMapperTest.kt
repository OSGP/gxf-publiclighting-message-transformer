// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper

import com.google.protobuf.ByteString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.Direction
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper.LogItemMessageMapper.toBase64
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper.LogItemMessageMapper.toIsIncoming

class LogItemMessageMapperTest {
    @Test
    fun `should map raw data to base64`() {
        val rawData = ByteString.copyFromUtf8("raw_message_data")
        val expected = "cmF3X21lc3NhZ2VfZGF0YQ=="

        val actual = rawData.toBase64()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map direction to is incoming`() {
        assertThat(Direction.FROM_DEVICE.toIsIncoming()).isEqualTo("true")
        assertThat(Direction.TO_DEVICE.toIsIncoming()).isEqualTo("false")
        assertThat(Direction.UNRECOGNIZED.toIsIncoming()).isNull()
    }
}
