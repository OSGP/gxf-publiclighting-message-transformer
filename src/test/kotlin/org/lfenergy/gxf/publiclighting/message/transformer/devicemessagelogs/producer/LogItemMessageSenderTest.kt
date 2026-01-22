// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.producer

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import jakarta.jms.ObjectMessage
import jakarta.jms.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lfenergy.gxf.publiclighting.contracts.internal.auditlogging.LogItemMessage
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DECODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ENCODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_IS_INCOMING
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_PAYLOAD_MESSAGE_SERIALIZED_SIZE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DECODED_MESSAGE
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ENCODED_MESSAGE_BASE_64
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.IS_INCOMING
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.PAYLOAD_SIZE
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.InboundLogItemMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.devicemessagelogs.config.DeviceMessageLogsConfigurationProperties
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

@ExtendWith(MockKExtension::class)
class LogItemMessageSenderTest {
    @MockK
    lateinit var jmsTemplate: JmsTemplate

    @MockK
    lateinit var properties: DeviceMessageLogsConfigurationProperties

    @InjectMockKs
    lateinit var logItemMessageSender: LogItemMessageSender

    private lateinit var objectMessage: ObjectMessage
    private lateinit var session: Session
    private lateinit var message: LogItemMessage

    @BeforeEach
    fun setupMocks() {
        session = mockk()
        objectMessage = spyk()
        every { session.createObjectMessage() } returns objectMessage
        every { properties.producer.outboundQueue } returns OUTBOUND_QUEUE_NAME
        every { jmsTemplate.send(any<String>(), any<MessageCreator>()) } answers {
            val creator = secondArg<MessageCreator>()
            creator.createMessage(session)
        }
    }

    @Test
    fun `should send log item message`() {
        message = InboundLogItemMessageFactory.protobufMessageForLogItem()

        logItemMessageSender.send(message)

        verifyObjectMessageIsSent()
        verifyObjectMessageProperties()
    }

    private fun verifyObjectMessageIsSent() {
        var creator: MessageCreator? = null
        verify { jmsTemplate.send(OUTBOUND_QUEUE_NAME, withArg { creator = it }) }
        val message = creator!!.createMessage(session)
        assertThat(message).isInstanceOf(ObjectMessage::class.java).isEqualTo(objectMessage)
    }

    private fun verifyObjectMessageProperties() {
        verify { objectMessage.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, DEVICE_IDENTIFICATION) }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, ORGANIZATION_IDENTIFICATION) }

        verify { objectMessage.setStringProperty(JMS_PROPERTY_IS_INCOMING, IS_INCOMING) }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_DECODED_MESSAGE, DECODED_MESSAGE) }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_ENCODED_MESSAGE, ENCODED_MESSAGE_BASE_64) }
        verify { objectMessage.setIntProperty(JMS_PROPERTY_PAYLOAD_MESSAGE_SERIALIZED_SIZE, PAYLOAD_SIZE) }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "outbound-queue"
    }
}
