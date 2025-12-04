// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ALL_RELAYS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.CORRELATION_UID
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ELEVEN_PM
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.HALF_HOUR_IN_SECONDS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.RELAY_THREE
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.RELAY_TWO
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.SIX_AM
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleEntryDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage

object InboundRequestMessageFactory {
    fun requestMessageForType(requestType: ObjectMessageType) =
        RequestMessage(
            CORRELATION_UID,
            ORGANIZATION_IDENTIFICATION,
            DEVICE_IDENTIFICATION,
            NETWORK_ADDRESS,
            null,
            null,
            when (requestType) {
                ObjectMessageType.GET_STATUS -> null
                ObjectMessageType.SET_LIGHT -> setLightRequestPayload()
                ObjectMessageType.SET_SCHEDULE -> setScheduleRequestPayload()
                else -> null
            },
        )

    fun setLightRequestPayload() =
        LightValueMessageDataContainerDto(
            listOf(
                LightValueDto(RELAY_TWO, true, null),
                LightValueDto(RELAY_THREE, true, null),
            ),
        )

    fun setScheduleRequestPayload() =
        ScheduleDto(
            null,
            null,
            mutableListOf(
                allLightsOnAtSunset(),
                allLightsOffAtSunrise(),
                eveningBurnerOffAt2300(),
                morningBurnerOnAt0600(),
            ),
        )

    fun allLightsOnAtSunset() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.SUNSET,
            null,
            null,
            mutableListOf(LightValueDto(ALL_RELAYS, true, null)),
            TriggerTypeDto.ASTRONOMICAL,
            null,
            null,
            null,
        )

    fun allLightsOffAtSunrise() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.SUNRISE,
            null,
            null,
            mutableListOf(LightValueDto(ALL_RELAYS, false, null)),
            TriggerTypeDto.ASTRONOMICAL,
            null,
            null,
            null,
        )

    fun eveningBurnerOffAt2300() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.ABSOLUTETIME,
            ELEVEN_PM,
            null,
            mutableListOf(LightValueDto(RELAY_THREE, false, null)),
            null,
            null,
            null,
            HALF_HOUR_IN_SECONDS,
        )

    fun morningBurnerOnAt0600() =
        ScheduleEntryDto(
            WeekDayTypeDto.ALL,
            null,
            null,
            ActionTimeTypeDto.ABSOLUTETIME,
            SIX_AM,
            null,
            mutableListOf(LightValueDto(RELAY_THREE, true, null)),
            null,
            null,
            null,
            HALF_HOUR_IN_SECONDS,
        )
}
