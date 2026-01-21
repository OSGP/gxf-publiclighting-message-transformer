// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper

import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.NotificationType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.ResumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetEventNotificationMaskRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.SetTransitionRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.TransitionType
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.deviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.lightValue
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.requestHeader
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.resumeScheduleRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setEventNotificationMaskRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setLightRequest
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.setTransitionRequest
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DOMAIN_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.CommonMappingFunctions.toRelayIndex
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.CommonMappingFunctions.toUtcTimeString
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.SetConfigurationRequestMapper.toProtobufMessage
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.mapper.SetScheduleRequestMapper.toProtobufMessage
import org.opensmartgridplatform.dto.valueobjects.ConfigurationDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationTypeDto
import org.opensmartgridplatform.dto.valueobjects.LightValueMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.ResumeScheduleMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.ScheduleDto
import org.opensmartgridplatform.dto.valueobjects.TransitionMessageDataContainerDto
import org.opensmartgridplatform.dto.valueobjects.TransitionTypeDto

object DeviceRequestMessageMapper {
    fun ObjectMessage.toProtobufMessage(): DeviceRequestMessage {
        val message = this
        val messageType = message.jmsType.toProtobufRequestType()

        return deviceRequestMessage {
            header =
                requestHeader {
                    correlationUid = message.jmsCorrelationID
                    deviceIdentification = message.getStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION)
                    domain = message.getStringProperty(JMS_PROPERTY_DOMAIN)
                    domainVersion = message.getStringProperty(JMS_PROPERTY_DOMAIN_VERSION)
                    networkAddress = message.getStringProperty(JMS_PROPERTY_NETWORK_ADDRESS)
                    organizationIdentification = message.getStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION)
                    requestType = messageType
                }
            when (messageType) {
                RequestType.GET_CONFIGURATION_REQUEST,
                RequestType.GET_FIRMWARE_VERSION_REQUEST,
                RequestType.GET_LIGHT_STATUS_REQUEST,
                RequestType.GET_STATUS_REQUEST,
                RequestType.REBOOT_REQUEST,
                RequestType.START_SELF_TEST_REQUEST,
                RequestType.STOP_SELF_TEST_REQUEST,
                -> { /* No payload for these requests */ }
                RequestType.SET_CONFIGURATION_REQUEST ->
                    setConfigurationRequest = (message.`object` as ConfigurationDto).toProtobufMessage()

                RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST ->
                    setEventNotificationMaskRequest =
                        (message.`object` as EventNotificationMessageDataContainerDto).toProtobufMessage()

                RequestType.RESUME_SCHEDULE_REQUEST ->
                    resumeScheduleRequest = (`object` as ResumeScheduleMessageDataContainerDto).toProtobufMessage()

                RequestType.SET_LIGHT_REQUEST ->
                    setLightRequest = (`object` as LightValueMessageDataContainerDto).toProtobufMessage()

                RequestType.SET_SCHEDULE_REQUEST ->
                    setScheduleRequest = (`object` as ScheduleDto).toProtobufMessage()

                RequestType.SET_TRANSITION_REQUEST ->
                    setTransitionRequest = (`object` as TransitionMessageDataContainerDto).toProtobufMessage()

                else -> throw IllegalArgumentException("Unsupported message type: $jmsType")
            }
        }
    }

    private fun String.toProtobufRequestType() =
        when (this) {
            "GET_CONFIGURATION" -> RequestType.GET_CONFIGURATION_REQUEST
            "GET_FIRMWARE_VERSION" -> RequestType.GET_FIRMWARE_VERSION_REQUEST
            "GET_LIGHT_STATUS" -> RequestType.GET_LIGHT_STATUS_REQUEST
            "GET_STATUS" -> RequestType.GET_STATUS_REQUEST
            "RESUME_SCHEDULE" -> RequestType.RESUME_SCHEDULE_REQUEST
            "SET_CONFIGURATION" -> RequestType.SET_CONFIGURATION_REQUEST
            "SET_EVENT_NOTIFICATIONS" -> RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST
            "SET_LIGHT" -> RequestType.SET_LIGHT_REQUEST
            "SET_REBOOT" -> RequestType.REBOOT_REQUEST
            "SET_SCHEDULE" -> RequestType.SET_SCHEDULE_REQUEST
            "SET_TRANSITION" -> RequestType.SET_TRANSITION_REQUEST
            "START_SELF_TEST" -> RequestType.START_SELF_TEST_REQUEST
            "STOP_SELF_TEST" -> RequestType.STOP_SELF_TEST_REQUEST
            else -> throw IllegalArgumentException("Unsupported message type: $this")
        }

    private fun EventNotificationMessageDataContainerDto.toProtobufMessage(): SetEventNotificationMaskRequest {
        val dto = this
        return setEventNotificationMaskRequest {
            notificationTypes.addAll(dto.eventNotifications.map { it.toProtobuf() })
        }
    }

    private fun EventNotificationTypeDto.toProtobuf() = NotificationType.valueOf(this.name)

    private fun LightValueMessageDataContainerDto.toProtobufMessage(): SetLightRequest {
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

    private fun ResumeScheduleMessageDataContainerDto.toProtobufMessage(): ResumeScheduleRequest {
        val dto = this
        return resumeScheduleRequest {
            dto.index?.let { index = it.toRelayIndex() }
            immediate = dto.immediate
        }
    }

    private fun TransitionMessageDataContainerDto.toProtobufMessage(): SetTransitionRequest {
        val dto = this
        return setTransitionRequest {
            dto.transitionType?.let { transitionType = it.toTransitionType() }
            dto.dateTime?.let { time = it.toUtcTimeString() }
        }
    }

    private fun TransitionTypeDto.toTransitionType() =
        when (this) {
            TransitionTypeDto.DAY_NIGHT -> TransitionType.SUNSET
            TransitionTypeDto.NIGHT_DAY -> TransitionType.SUNRISE
        }
}
