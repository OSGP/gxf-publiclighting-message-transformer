// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.lfenergy.gxf.publiclighting.contracts.internal.audittrail.MessageType
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.mapper.LogItemMessageMapper.toIsIncoming

class LogItemMessageMapperTest {
    // TODO - add tests for mapping decoded and encoded messages

    @Test
    fun `should map direction to is incoming`() {
        assertThat(MessageType.FROM_DEVICE.toIsIncoming()).isEqualTo("true")
        assertThat(MessageType.TO_DEVICE.toIsIncoming()).isEqualTo("false")
        assertThatIllegalArgumentException().isThrownBy { MessageType.UNRECOGNIZED.toIsIncoming() }
    }
}
