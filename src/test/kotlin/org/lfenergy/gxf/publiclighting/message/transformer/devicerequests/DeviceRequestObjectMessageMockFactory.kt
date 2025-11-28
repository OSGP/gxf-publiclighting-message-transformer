// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests

import io.mockk.every
import io.mockk.mockk
import jakarta.jms.ObjectMessage
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants

object DeviceRequestObjectMessageMockFactory {
    const val REQUEST_TYPE_SET_LIGHT = "SET_LIGHT"
    const val REQUEST_TYPE_GET_STATUS = "GET_STATUS"

    fun deviceRequestObjectMessageMock(requestType: String): ObjectMessage {
        val objectMessage = mockk<ObjectMessage>()
        every { objectMessage.jmsCorrelationID } returns TestConstants.CORRELATION_UID
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION) } returns
            TestConstants.DEVICE_IDENTIFICATION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION) } returns
            TestConstants.ORGANIZATION_IDENTIFICATION
        every { objectMessage.getStringProperty(ApplicationConstants.JMS_PROPERTY_NETWORK_ADDRESS) } returns TestConstants.NETWORK_ADDRESS
        when (requestType) {
            REQUEST_TYPE_SET_LIGHT -> setupSetLightDeviceRequestMessage(objectMessage)
            REQUEST_TYPE_GET_STATUS -> setUpGetStatusDeviceRequestMessage(objectMessage)
            else -> throw IllegalArgumentException("Unsupported request type: $requestType")
        }
        return objectMessage
    }

    private fun setupSetLightDeviceRequestMessage(objectMessage: ObjectMessage) {
        every { objectMessage.jmsType } returns REQUEST_TYPE_SET_LIGHT
        every { objectMessage.`object` } returns RequestMessageFactory.setLightRequestPayload()
    }

    private fun setUpGetStatusDeviceRequestMessage(objectMessage: ObjectMessage) {
        every { objectMessage.jmsType } returns REQUEST_TYPE_GET_STATUS
        every { objectMessage.`object` } returns null
    }
}
