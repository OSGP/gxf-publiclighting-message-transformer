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
     * <p>The serialized input files located in {@code src/test/resources/} are produced in the
     * platform project by running the CreateSerializedObjectsTest.
     * Copy those files into this project's {@code src/test/resources/} directory.
     */

    @Test
    fun `should deserialize set schedule dto serialized by java`() {
        val dto =
            javaClass
                .getResourceAsStream("/set-schedule-request.ser")
                .use { ObjectInputStream(it).readObject() } as ScheduleDto

        assertThat(dto.scheduleList).isNotNull
        val schedules = dto.scheduleList!!
        assertThat(schedules).hasSize(2)

        assertThat(schedules[0]).isNotNull
        val scheduleOn = schedules[0]!!
        assertThat(scheduleOn.weekDay).isEqualTo(WeekDayTypeDto.ALL)
        assertThat(scheduleOn.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
        assertThat(scheduleOn.actionTime).isEqualTo(ActionTimeTypeDto.SUNSET)

        assertThat(scheduleOn.lightValue).isNotNull
        val lightValuesOn = scheduleOn.lightValue!!
        assertThat(lightValuesOn).hasSize(1)

        assertThat(lightValuesOn[0]).isNotNull
        val lightValueOn = lightValuesOn[0]!!
        assertThat(lightValueOn.index).isEqualTo(2)
        assertThat(lightValueOn.on).isEqualTo(true)

        assertThat(schedules[1]).isNotNull
        val scheduleOff = schedules[1]!!
        assertThat(scheduleOff.weekDay).isEqualTo(WeekDayTypeDto.ALL)
        assertThat(scheduleOff.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
        assertThat(scheduleOff.actionTime).isEqualTo(ActionTimeTypeDto.SUNRISE)

        assertThat(scheduleOff.lightValue).isNotNull
        val lightValuesOff = scheduleOff.lightValue!!
        assertThat(lightValuesOff).hasSize(1)

        assertThat(lightValuesOff[0]).isNotNull
        val lightValueOff = lightValuesOff[0]!!
        assertThat(lightValueOff.index).isEqualTo(2)
        assertThat(lightValueOff.on).isEqualTo(false)
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

        assertThat(dto.deviceFixedIp).isNotNull
        assertThat(dto.deviceFixedIp?.ipAddress).isEqualTo("ipAddress1")
        assertThat(dto.deviceFixedIp?.netMask).isEqualTo("netMask1")
        assertThat(dto.deviceFixedIp?.gateWay).isEqualTo("gateWay1")

        assertThat(dto.switchingDelays).containsExactly(142, 143)

        assertThat(dto.relayLinking).isNotNull
        val relayLinks = dto.relayLinking!!
        assertThat(relayLinks).hasSize(2)
        assertThat(relayLinks[0]).isNotNull
        val relayLink1 = relayLinks[0]!!
        assertThat(relayLink1.masterRelayIndex).isEqualTo(144)
        assertThat(relayLink1.masterRelayOn).isTrue
        assertThat(relayLinks[1]).isNotNull
        val relayLink2 = relayLinks[1]!!
        assertThat(relayLink2.masterRelayIndex).isEqualTo(145)
        assertThat(relayLink2.masterRelayOn).isFalse

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
