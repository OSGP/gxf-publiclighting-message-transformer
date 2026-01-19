// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.serialization

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
import org.opensmartgridplatform.dto.valueobjects.EventTypeDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import java.io.ObjectInputStream
import java.time.ZonedDateTime

class RequestDeserializationTest {
    /**
     * Verifies that objects serialized by the
     * open-smart-grid-platform project can be successfully
     * deserialized and interpreted in this project.
     *
     * <p>The serialized input files located in {@code src/test/resources/} are produced in the
     * platform project by running the CreateSerializedObjectsTest.
     * Copy those files into this project's {@code src/test/resources/} directory.
     */

    @Test
    fun `should deserialize set schedule request serialized by java`() {
        val msg =
            javaClass
                .getResourceAsStream("/set-schedule-request.ser")
                .use { ObjectInputStream(it).readObject() } as ScheduleDto

        val scheduleOn = msg.scheduleList!![0]
        assertThat(scheduleOn!!.weekDay).isEqualTo(WeekDayTypeDto.ALL)
        assertThat(scheduleOn.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
        assertThat(scheduleOn.actionTime).isEqualTo(ActionTimeTypeDto.SUNSET)
        assertThat(scheduleOn.lightValue!![0]!!.index).isEqualTo(2)
        assertThat(scheduleOn.lightValue[0]!!.on).isEqualTo(true)

        val scheduleOff = msg.scheduleList[1]
        assertThat(scheduleOff!!.weekDay).isEqualTo(WeekDayTypeDto.ALL)
        assertThat(scheduleOff.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
        assertThat(scheduleOff.actionTime).isEqualTo(ActionTimeTypeDto.SUNRISE)
        assertThat(scheduleOff.lightValue!![0]!!.index).isEqualTo(2)
        assertThat(scheduleOff.lightValue[0]!!.on).isEqualTo(false)
    }

    @Test
    fun `should deserialize event notification request serialized by java`() {
        val msg =
            javaClass
                .getResourceAsStream("/event-notification-request.ser")
                .use { ObjectInputStream(it).readObject() } as EventNotificationDto

        assertThat(msg.deviceUid).isEqualTo("testUid")
        assertThat(msg.eventType).isEqualTo(EventTypeDto.LIGHT_SENSOR_REPORTS_LIGHT)
        assertThat(msg.description).isEqualTo("Sensor reports light")
        assertThat(msg.index).isEqualTo(0)
        assertThat(msg.dateTime).isEqualTo(ZonedDateTime.parse("2026-01-01T12:00:00+00:00"))
    }
}
