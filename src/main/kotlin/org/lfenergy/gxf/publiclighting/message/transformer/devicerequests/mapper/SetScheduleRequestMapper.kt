// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ActionTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.LightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ScheduleEntry
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TriggerType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TriggerWindow
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.Weekday
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.scheduleEntry
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.triggerWindow
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.CommonMappingFunctions.toProtobuf
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.CommonMappingFunctions.toRelayIndex
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleEntryDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import org.opensmartgridplatform.dto.valueobjects.WindowTypeDto

object SetScheduleRequestMapper {
    fun ScheduleDto.toProtobufMessage(): SetScheduleRequest {
        val dto = this
        if (dto.scheduleList.isNullOrEmpty()) {
            return setScheduleRequest {}
        }
        return setScheduleRequest {
            dto.astronomicalSunsetOffset?.let { astronomicalSunsetOffset = it.toInt() }
            dto.astronomicalSunriseOffset?.let { astronomicalSunriseOffset = it.toInt() }
            scheduleEntries.addAll(
                dto.scheduleList.map { scheduleEntryDto ->
                    scheduleEntryDto!!.toProtobufMessage()
                },
            )
        }
    }

    private fun ScheduleEntryDto.toProtobufMessage(): ScheduleEntry {
        val dto = this
        return scheduleEntry {
            dto.weekDay?.let { weekday = dto.weekDay.toProtobuf() }
            dto.startDay?.let { startDay = it.toProtobuf() }
            dto.endDay?.let { endDay = it.toProtobuf() }
            dto.actionTime?.let { actionTime = dto.actionTime.toProtobuf() }
            dto.time?.let { time = it }
            dto.triggerWindow?.let { window = triggerWindow { it.toProtobuf() } }
            dto.lightValue?.let { value.addAll(dto.lightValue.map { it!!.toProtobuf() }) }
            dto.triggerType?.let { triggerType = dto.triggerType.toProtobuf() }
            dto.index?.let { index = it }
            // TODO - Is isEnabled needed in schedule entry?
//            dto.isEnabled?.let { enabled = it }
            dto.minimumLightsOn?.let { minimumLightsOn = it }
        }
    }

    private fun WindowTypeDto.toProtobuf(): TriggerWindow {
        val dto = this
        return triggerWindow {
            minutesBefore = dto.minutesBefore.toInt()
            minutesAfter = dto.minutesAfter.toInt()
        }
    }

    private fun WeekDayTypeDto.toProtobuf() =
        when (this) {
            WeekDayTypeDto.MONDAY -> Weekday.MONDAY
            WeekDayTypeDto.TUESDAY -> Weekday.TUESDAY
            WeekDayTypeDto.WEDNESDAY -> Weekday.WEDNESDAY
            WeekDayTypeDto.THURSDAY -> Weekday.THURSDAY
            WeekDayTypeDto.FRIDAY -> Weekday.FRIDAY
            WeekDayTypeDto.SATURDAY -> Weekday.SATURDAY
            WeekDayTypeDto.SUNDAY -> Weekday.SUNDAY
            WeekDayTypeDto.WEEKDAY -> Weekday.WEEKDAY
            WeekDayTypeDto.WEEKEND -> Weekday.WEEKEND
            WeekDayTypeDto.ALL -> Weekday.ALL_DAYS
            WeekDayTypeDto.ABSOLUTEDAY -> Weekday.ABSOLUTE_DAY
        }

    private fun ActionTimeTypeDto.toProtobuf() =
        when (this) {
            ActionTimeTypeDto.SUNSET -> ActionTime.SUNSET_TIME
            ActionTimeTypeDto.SUNRISE -> ActionTime.SUNRISE_TIME
            ActionTimeTypeDto.ABSOLUTETIME -> ActionTime.ABSOLUTE_TIME
        }

    private fun TriggerTypeDto.toProtobuf() =
        when (this) {
            TriggerTypeDto.ASTRONOMICAL -> TriggerType.ASTRONOMICAL
            TriggerTypeDto.LIGHT_TRIGGER -> TriggerType.LIGHT_TRANSITION
        }

    private fun LightValueDto.toProtobuf(): LightValue {
        val dto = this
        return lightValue {
            index = dto.index!!.toRelayIndex()
            lightOn = dto.on
        }
    }
}
