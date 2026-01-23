// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import io.mockk.every
import io.mockk.mockk
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.GET_CONFIGURATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.GET_FIRMWARE_VERSION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.GET_LIGHT_STATUS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.GET_STATUS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.RESUME_SCHEDULE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.SET_CONFIGURATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.SET_EVENT_NOTIFICATIONS
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.SET_LIGHT
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.SET_LIGHT_SCHEDULE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.SET_REBOOT
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.SET_TRANSITION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.START_SELF_TEST
import org.lfenergy.gxf.publiclighting.message.transformer.common.ObjectMessageType.STOP_SELF_TEST
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants
import java.io.Serializable

object DeviceRequestObjectMessageMockFactory {
    fun deviceRequestObjectMessageMock(requestType: ObjectMessageType) =
        when (requestType) {
            GET_CONFIGURATION -> setUpDeviceRequestMessage(requestType, null)
            GET_FIRMWARE_VERSION -> setUpDeviceRequestMessage(requestType, null)
            GET_LIGHT_STATUS -> setUpDeviceRequestMessage(requestType, null)
            GET_STATUS -> setUpDeviceRequestMessage(requestType, null)
            RESUME_SCHEDULE -> setUpDeviceRequestMessage(requestType, InboundRequestMessageFactory.setResumeScheduleRequestPayload())
            SET_CONFIGURATION -> setUpDeviceRequestMessage(requestType, InboundRequestMessageFactory.setConfigurationRequestPayload())
            SET_EVENT_NOTIFICATIONS ->
                setUpDeviceRequestMessage(
                    requestType,
                    InboundRequestMessageFactory.setEventNotificationsRequestPayload(),
                )
            SET_LIGHT -> setUpDeviceRequestMessage(requestType, InboundRequestMessageFactory.setLightRequestPayload())
            SET_REBOOT -> setUpDeviceRequestMessage(requestType, null)
            SET_LIGHT_SCHEDULE -> setUpDeviceRequestMessage(requestType, InboundRequestMessageFactory.setScheduleRequestPayload())
            SET_TRANSITION -> setUpDeviceRequestMessage(requestType, InboundRequestMessageFactory.setTransitionRequestPayload())
            START_SELF_TEST -> setUpDeviceRequestMessage(requestType, null)
            STOP_SELF_TEST -> setUpDeviceRequestMessage(requestType, null)
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
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_DOMAIN) } returns TestConstants.DOMAIN
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_DOMAIN_VERSION) } returns TestConstants.DOMAIN_VERSION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS) } returns TestConstants.NETWORK_ADDRESS
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION) } returns
            TestConstants.ORGANIZATION_IDENTIFICATION
        every { objectMessage.jmsType } returns objectMessageType.name
        every { objectMessage.`object` } returns payload
        return objectMessage
    }
}
