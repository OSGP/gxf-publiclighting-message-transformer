// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import jakarta.jms.ObjectMessage
import org.joda.time.DateTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ActionTime
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.LightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RelayIndex
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ResumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ScheduleEntry
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetTransitionRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TransitionType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TriggerType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TriggerWindow
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.Weekday
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.deviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.requestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.resumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.scheduleEntry
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setTransitionRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.triggerWindow
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.opensmartgridplatform.dto.valueobjects.ActionTimeTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueDto
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.ResumeScheduleMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleEntryDto
import org.opensmartgridplatform.dto.valueobjects.TransitionMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.TransitionTypeDto
import org.opensmartgridplatform.dto.valueobjects.TriggerTypeDto
import org.opensmartgridplatform.dto.valueobjects.WeekDayTypeDto
import org.opensmartgridplatform.dto.valueobjects.WindowTypeDto
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DeviceRequestMessageMapper {
    val hhmmssFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss")

    fun ObjectMessage.toProtobufMessage(): DeviceRequestMessage {
        val message = this
        val messageType = message.jmsType.toProtobufRequestType()

        return deviceRequestMessage {
            header =
                requestHeader {
                    deviceIdentification = message.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
                    correlationUid = message.jmsCorrelationID
                    organizationIdentification = message.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION)
                    networkAddress = message.getStringProperty(JMS_PROPERTY_NETWORK_ADDRESS)
                    requestType = messageType
                }
            when (messageType) {
                RequestType.GET_STATUS_REQUEST -> { /* No payload for get status request */ }
                RequestType.REBOOT_REQUEST -> { /* No payload for reboot request */ }
                RequestType.RESUME_SCHEDULE_REQUEST ->
                    resumeScheduleRequest =
                        (`object` as ResumeScheduleMessageDataContainerDto).toProtobufMessage()
                RequestType.SET_LIGHT_REQUEST -> setLightRequest = (`object` as LightValueMessageDataContainerDto).toProtobufMessage()
                RequestType.SET_SCHEDULE_REQUEST -> setScheduleRequest = (`object` as ScheduleDto).toProtobufMessage()
                RequestType.SET_TRANSITION_REQUEST ->
                    setTransitionRequest =
                        (`object` as TransitionMessageDataContainerDto).toProtobufMessage()
                RequestType.START_SELF_TEST_REQUEST -> { /* No payload for start self test request */ }
                RequestType.STOP_SELF_TEST_REQUEST -> { /* No payload for stop self test request */ }
                else -> throw IllegalArgumentException("Unsupported message type: $jmsType")
            }
        }
    }

    fun String.toProtobufRequestType() =
        when (this) {
            "GET_STATUS" -> RequestType.GET_STATUS_REQUEST
            "RESUME_SCHEDULE" -> RequestType.RESUME_SCHEDULE_REQUEST
            "SET_LIGHT" -> RequestType.SET_LIGHT_REQUEST
            "SET_REBOOT" -> RequestType.REBOOT_REQUEST
            "SET_SCHEDULE" -> RequestType.SET_SCHEDULE_REQUEST
            "SET_TRANSITION" -> RequestType.SET_TRANSITION_REQUEST
            "START_SELF_TEST" -> RequestType.START_SELF_TEST_REQUEST
            "STOP_SELF_TEST" -> RequestType.STOP_SELF_TEST_REQUEST
            else -> throw IllegalArgumentException("Unsupported message type: $this")
        }

    fun LightValueMessageDataContainerDto.toProtobufMessage(): SetLightRequest {
        val dto = this
        return setLightRequest {
            lightValues.addAll(
                dto.lightValues.map { lightValueDto ->
                    lightValue {
                        index = lightValueDto.index!!.toRelayIndex()
                        lightOn = lightValueDto.on
                    }
                },
            )
        }
    }

    fun ResumeScheduleMessageDataContainerDto.toProtobufMessage(): ResumeScheduleRequest {
        val dto = this
        return resumeScheduleRequest {
            dto.index?.let { index = it.toRelayIndex() }
            immediate = dto.immediate
        }
    }

    fun ScheduleDto.toProtobufMessage(): SetScheduleRequest {
        val dto = this
        if (dto.scheduleList == null || dto.scheduleList.isEmpty()) {
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

    fun TransitionMessageDataContainerDto.toProtobufMessage(): SetTransitionRequest {
        val dto = this
        return setTransitionRequest {
            dto.transitionType?.let { transitionType = it.toTransitionType() }
            dto.dateTime?.let { time = it.toUtcTimeString() }
        }
    }

    fun ZonedDateTime.toUtcTimeString(): String = this.withZoneSameInstant(ZoneOffset.UTC).format(hhmmssFormatter)

    fun TransitionTypeDto.toTransitionType() =
        when (this) {
            TransitionTypeDto.DAY_NIGHT -> TransitionType.SUNSET
            TransitionTypeDto.NIGHT_DAY -> TransitionType.SUNRISE
        }

    fun ScheduleEntryDto.toProtobufMessage(): ScheduleEntry {
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
//            dto.isEnabled?.let { enabled = it }
            dto.minimumLightsOn?.let { minimumLightsOn = it }
        }
    }

    fun WindowTypeDto.toProtobuf(): TriggerWindow {
        val dto = this
        return triggerWindow {
            minutesBefore = dto.minutesBefore.toInt()
            minutesAfter = dto.minutesAfter.toInt()
        }
    }

    fun DateTime.toProtobuf(): String = this.toString("HHmmss")

    fun WeekDayTypeDto.toProtobuf() =
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

    fun ActionTimeTypeDto.toProtobuf() =
        when (this) {
            ActionTimeTypeDto.SUNSET -> ActionTime.SUNSET_TIME
            ActionTimeTypeDto.SUNRISE -> ActionTime.SUNRISE_TIME
            ActionTimeTypeDto.ABSOLUTETIME -> ActionTime.ABSOLUTE_TIME
        }

    fun TriggerTypeDto.toProtobuf() =
        when (this) {
            TriggerTypeDto.ASTRONOMICAL -> TriggerType.ASTRONOMICAL
            TriggerTypeDto.LIGHT_TRIGGER -> TriggerType.LIGHT_TRANSITION
        }

    fun LightValueDto.toProtobuf(): LightValue {
        val dto = this
        return lightValue {
            index = dto.index!!.toRelayIndex()
            lightOn = dto.on
        }
    }

    fun Int.toRelayIndex() =
        when (this) {
            0 -> RelayIndex.ALL_RELAYS
            1 -> RelayIndex.RELAY_ONE
            2 -> RelayIndex.RELAY_TWO
            3 -> RelayIndex.RELAY_THREE
            4 -> RelayIndex.RELAY_FOUR
            else -> throw IllegalArgumentException("Unsupported relay index: $this")
        }
}
