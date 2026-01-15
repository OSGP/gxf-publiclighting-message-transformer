// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import java.io.ObjectInputStream

class RequestDeserializationTest {
    @Test
    fun `should deserialize set schedule request serialized by java`() {
        val inputStream = javaClass.getResourceAsStream("/set-schedule-request.ser")!!

        val obj =
            ObjectInputStream(inputStream).use {
                it.readObject()
            }

        val msg = obj as ScheduleDto

        assertThat(msg).isNotNull
    }
}
