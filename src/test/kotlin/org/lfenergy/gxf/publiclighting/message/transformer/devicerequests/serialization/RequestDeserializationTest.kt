// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.serialization

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import java.io.ObjectInputStream
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class RequestDeserializationTest {
    /**
     * Verifies that objects serialized by the
     * open-smart-grid-platform project can be successfully
     * deserialized and interpreted in this project.
     *
     * The serialized input files located in {@code src/test/resources/} are produced in the
     * platform project by running the CreateSerializedObjectsTest.
     * Copy those files into this project's {@code src/test/resources/} directory.
     */

    @Test
    fun `should deserialize set schedule dto serialized by java`() {
        val dto =
            javaClass
                .getResourceAsStream("/set-schedule-request.ser")
                .use { ObjectInputStream(it).readObject() } as ScheduleDto

        assertThat(dto.scheduleList).isNotNull.hasSize(2)
        val schedules = dto.scheduleList!!

        assertThat(schedules[0]).isNotNull().satisfies({
            it!!
            assertThat(it.weekDay).isEqualTo(WeekDayTypeDto.ALL)
            assertThat(it.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
            assertThat(it.actionTime).isEqualTo(ActionTimeTypeDto.SUNSET)

            assertThat(it.lightValue).isNotNull().hasSize(1)
            assertThat(it.lightValue).element(0).isNotNull().satisfies({ lv ->
                lv!!
                assertThat(lv.index).isEqualTo(2)
                assertThat(lv.on).isTrue
            })
        })

        assertThat(schedules[1]).isNotNull().satisfies({
            it!!
            assertThat(it.weekDay).isEqualTo(WeekDayTypeDto.ALL)
            assertThat(it.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
            assertThat(it.actionTime).isEqualTo(ActionTimeTypeDto.SUNRISE)

            assertThat(it.lightValue).isNotNull().hasSize(1)
            assertThat(it.lightValue).element(0).isNotNull().satisfies({ lv ->
                lv!!
                assertThat(lv.index).isEqualTo(2)
                assertThat(lv.on).isFalse
            })
        })
    }

    @Test
    fun `should deserialize configuration dto serialized by java`() {
        val dto =
            javaClass
                .getResourceAsStream("/configuration-request.ser")
                .use { ObjectInputStream(it).readObject() } as ConfigurationDto

        assertThat(dto.lightType).isEqualTo(LightTypeDto.RELAY)
        assertThat(dto.daliConfiguration).isNotNull
        assertThat(dto.relayConfiguration).isNotNull
        assertThat(dto.preferredLinkType).isEqualTo(LinkTypeDto.CDMA)

        assertThat(dto.timeSyncFrequency).isEqualTo(133)
        assertThat(dto.dhcpEnabled).isTrue
        assertThat(dto.tlsEnabled).isTrue
        assertThat(dto.tlsPortNumber).isEqualTo(134)
        assertThat(dto.commonNameString).isEqualTo("commonNameString1")
        assertThat(dto.communicationTimeout).isEqualTo(135)
        assertThat(dto.communicationNumberOfRetries).isEqualTo(136)
        assertThat(dto.communicationPauseTimeBetweenConnectionTrials).isEqualTo(137)
        assertThat(dto.osgpIpAddress).isEqualTo("osgpIpAddress1")
        assertThat(dto.osgpPortNumber).isEqualTo(138)
        assertThat(dto.ntpHost).isEqualTo("ntpHost1")
        assertThat(dto.ntpEnabled).isTrue
        assertThat(dto.ntpSyncInterval).isEqualTo(139)
        assertThat(dto.testButtonEnabled).isTrue
        assertThat(dto.automaticSummerTimingEnabled).isTrue
        assertThat(dto.astroGateSunRiseOffset).isEqualTo(140)
        assertThat(dto.astroGateSunSetOffset).isEqualTo(141)
        assertThat(dto.relayRefreshing).isTrue

        assertThat(dto.deviceFixedIp).isNotNull.satisfies({
            it!!
            assertThat(it.ipAddress).isEqualTo("ipAddress1")
            assertThat(it.netMask).isEqualTo("netMask1")
            assertThat(it.gateWay).isEqualTo("gateWay1")
        })
        assertThat(dto.switchingDelays).containsExactly(142, 143)

        assertThat(dto.relayLinking).isNotNull.hasSize(2)

        assertThat(dto.relayLinking).element(0).isNotNull.satisfies({
            it!!
            assertThat(it.masterRelayIndex).isEqualTo(144)
            assertThat(it.masterRelayOn).isTrue
        })
        assertThat(dto.relayLinking).element(1).isNotNull.satisfies({
            it!!
            assertThat(it.masterRelayIndex).isEqualTo(145)
            assertThat(it.masterRelayOn).isFalse
        })

        val expectedSummer =
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(146L * 24 * 60 * 60 * 1000),
                ZoneId.systemDefault(),
            )
        val expectedWinter =
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(147L * 24 * 60 * 60 * 1000),
                ZoneId.systemDefault(),
            )

        assertThat(dto.summerTimeDetails).isEqualTo(expectedSummer)
        assertThat(dto.winterTimeDetails).isEqualTo(expectedWinter)
    }
}
