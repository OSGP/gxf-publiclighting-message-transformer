// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import io.mockk.every
import io.mockk.mockk
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.GET_FIRMWARE_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.GET_STATUS
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.RESUME_SCHEDULE
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.SET_EVENT_NOTIFICATIONS
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.SET_LIGHT
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.SET_REBOOT
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.SET_SCHEDULE
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.SET_TRANSITION
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.START_SELF_TEST
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType.STOP_SELF_TEST
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants
import java.io.Serializable

object DeviceRequestObjectMessageMockFactory {
    fun deviceRequestObjectMessageMock(requestType: ObjectMessageType) =
        when (requestType) {
            GET_FIRMWARE_VERSION -> setUpDeviceRequestMessage(ObjectMessageType.GET_FIRMWARE_VERSION, null)
            GET_STATUS -> setUpDeviceRequestMessage(GET_STATUS, null)
            RESUME_SCHEDULE -> setUpDeviceRequestMessage(RESUME_SCHEDULE, InboundRequestMessageFactory.setResumeScheduleRequestPayload())
            SET_EVENT_NOTIFICATIONS ->
                setUpDeviceRequestMessage(
                    SET_EVENT_NOTIFICATIONS,
                    InboundRequestMessageFactory.setEventNotificationsRequestPayload(),
                )
            SET_LIGHT -> setUpDeviceRequestMessage(SET_LIGHT, InboundRequestMessageFactory.setLightRequestPayload())
            SET_REBOOT -> setUpDeviceRequestMessage(SET_REBOOT, null)
            SET_SCHEDULE -> setUpDeviceRequestMessage(SET_SCHEDULE, InboundRequestMessageFactory.setScheduleRequestPayload())
            SET_TRANSITION -> setUpDeviceRequestMessage(SET_TRANSITION, InboundRequestMessageFactory.setTransitionRequestPayload())
            START_SELF_TEST -> setUpDeviceRequestMessage(START_SELF_TEST, null)
            STOP_SELF_TEST -> setUpDeviceRequestMessage(STOP_SELF_TEST, null)
            else -> throw IllegalArgumentException("Unsupported request type: $requestType")
        }

    private fun setUpDeviceRequestMessage(
        objectMessageType: ObjectMessageType,
        payload: Serializable?,
    ): ObjectMessage {
        val objectMessage = mockk<ObjectMessage>()
        every { objectMessage.jmsCorrelationID } returns TestConstants.CORRELATION_UID
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION) } returns
            TestConstants.DEVICE_IDENTIFICATION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION) } returns
            TestConstants.ORGANIZATION_IDENTIFICATION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS) } returns TestConstants.NETWORK_ADDRESS

        every { objectMessage.jmsType } returns objectMessageType.name
        every { objectMessage.`object` } returns payload
        return objectMessage
    }
}
