// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import io.mockk.every
import io.mockk.mockk
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.message.transformer.ObjectMessageType
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants

object DeviceRequestObjectMessageMockFactory {
    fun deviceRequestObjectMessageMock(requestType: ObjectMessageType): ObjectMessage {
        val objectMessage = mockk<ObjectMessage>()
        every { objectMessage.jmsCorrelationID } returns TestConstants.CORRELATION_UID
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION) } returns
            TestConstants.DEVICE_IDENTIFICATION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION) } returns
            TestConstants.ORGANIZATION_IDENTIFICATION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS) } returns TestConstants.NETWORK_ADDRESS
        when (requestType) {
            ObjectMessageType.GET_STATUS -> setUpGetStatusDeviceRequestMessage(objectMessage)
            ObjectMessageType.SET_LIGHT -> setupSetLightDeviceRequestMessage(objectMessage)
            ObjectMessageType.SET_SCHEDULE -> setupSetScheduleDeviceRequestMessage(objectMessage)
            else -> throw IllegalArgumentException("Unsupported request type: $requestType")
        }
        return objectMessage
    }

    private fun setUpGetStatusDeviceRequestMessage(objectMessage: ObjectMessage) {
        every { objectMessage.jmsType } returns ObjectMessageType.GET_STATUS.name
        every { objectMessage.`object` } returns null
    }

    private fun setupSetLightDeviceRequestMessage(objectMessage: ObjectMessage) {
        every { objectMessage.jmsType } returns ObjectMessageType.SET_LIGHT.name
        every { objectMessage.`object` } returns RequestMessageFactory.setLightRequestPayload()
    }

    private fun setupSetScheduleDeviceRequestMessage(objectMessage: ObjectMessage) {
        every { objectMessage.jmsType } returns ObjectMessageType.SET_SCHEDULE.name
        every { objectMessage.`object` } returns RequestMessageFactory.setScheduleRequestPayload()
    }
}
