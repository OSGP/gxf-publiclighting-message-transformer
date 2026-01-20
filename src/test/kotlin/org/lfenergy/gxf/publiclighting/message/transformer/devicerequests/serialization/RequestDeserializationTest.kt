// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.serialization

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
import org.opensmartgridplatform.dto.valueobjects.EventTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightTypeDto
import org.opensmartgridplatform.dto.valueobjects.LinkTypeDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import java.io.ObjectInputStream
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.Instant

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

        assertNotNull(msg.scheduleList)
        val scheduleOn = msg.scheduleList[0]
        assertNotNull(scheduleOn)
        assertThat(scheduleOn.weekDay).isEqualTo(WeekDayTypeDto.ALL)
        assertThat(scheduleOn.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
        assertThat(scheduleOn.actionTime).isEqualTo(ActionTimeTypeDto.SUNSET)
        assertNotNull(scheduleOn.lightValue)
        assertThat(scheduleOn.lightValue[0]?.index).isEqualTo(2)
        assertThat(scheduleOn.lightValue[0]?.on).isEqualTo(true)

        val scheduleOff = msg.scheduleList[1]
        assertNotNull(scheduleOff)
        assertThat(scheduleOff.weekDay).isEqualTo(WeekDayTypeDto.ALL)
        assertThat(scheduleOff.triggerType).isEqualTo(TriggerTypeDto.ASTRONOMICAL)
        assertThat(scheduleOff.actionTime).isEqualTo(ActionTimeTypeDto.SUNRISE)
        assertNotNull(scheduleOff.lightValue)
        assertThat(scheduleOff.lightValue[0]?.index).isEqualTo(2)
        assertThat(scheduleOff.lightValue[0]?.on).isEqualTo(false)
    }

    @Test
    fun `should deserialize configuration request serialized by java`() {
        val msg =
            javaClass
                .getResourceAsStream("/configuration-request.ser")
                .use { ObjectInputStream(it).readObject() } as ConfigurationDto


        assertThat(msg.lightType).isEqualTo(LightTypeDto.RELAY)
        assertThat(msg.daliConfiguration).isNotNull
        assertThat(msg.relayConfiguration).isNotNull
        assertThat(msg.preferredLinkType).isEqualTo(LinkTypeDto.CDMA)

        assertThat(msg.timeSyncFrequency).isEqualTo(133)
        assertThat(msg.dhcpEnabled).isTrue
        assertThat(msg.tlsEnabled).isTrue
        assertThat(msg.tlsPortNumber).isEqualTo(134)
        assertThat(msg.commonNameString).isEqualTo("commonNameString1")
        assertThat(msg.communicationTimeout).isEqualTo(135)
        assertThat(msg.communicationNumberOfRetries).isEqualTo(136)
        assertThat(msg.communicationPauseTimeBetweenConnectionTrials).isEqualTo(137)
        assertThat(msg.osgpIpAddress).isEqualTo("osgpIpAddress1")
        assertThat(msg.osgpPortNumber).isEqualTo(138)
        assertThat(msg.ntpHost).isEqualTo("ntpHost1")
        assertThat(msg.ntpEnabled).isTrue
        assertThat(msg.ntpSyncInterval).isEqualTo(139)
        assertThat(msg.testButtonEnabled).isTrue
        assertThat(msg.automaticSummerTimingEnabled).isTrue
        assertThat(msg.astroGateSunRiseOffset).isEqualTo(140)
        assertThat(msg.astroGateSunSetOffset).isEqualTo(141)
        assertThat(msg.relayRefreshing).isTrue

        assertNotNull(msg.deviceFixedIp)
        assertThat(msg.deviceFixedIp.ipAddress).isEqualTo("ipAddress1")
        assertThat(msg.deviceFixedIp.netMask).isEqualTo("netMask1")
        assertThat(msg.deviceFixedIp.gateWay).isEqualTo("gateWay1")

        assertThat(msg.switchingDelays).containsExactly(142, 143)

        assertThat(msg.relayLinking).hasSize(2)
        val relayLinks = msg.relayLinking
        assertNotNull(relayLinks)
        assertThat(relayLinks[0]?.masterRelayIndex).isEqualTo(144)
        assertThat(relayLinks[0]?.masterRelayOn).isTrue
        assertThat(relayLinks[1]?.masterRelayIndex).isEqualTo(145)
        assertThat(relayLinks[1]?.masterRelayOn).isFalse

        val expectedSummer = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(146L * 24 * 60 * 60 * 1000), ZoneId.systemDefault()
        )
        val expectedWinter = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(147L * 24 * 60 * 60 * 1000), ZoneId.systemDefault()
        )

        assertThat(msg.summerTimeDetails).isEqualTo(expectedSummer)
        assertThat(msg.winterTimeDetails).isEqualTo(expectedWinter)
    }
}
