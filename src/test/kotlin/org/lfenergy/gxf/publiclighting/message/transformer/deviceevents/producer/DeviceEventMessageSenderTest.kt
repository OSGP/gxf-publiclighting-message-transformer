// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.producer

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
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.DeviceEventMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_events.EventType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.DeviceEventMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.config.DeviceEventsConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceevents.mapper.DeviceEventMessageMapper.toMessageType
import org.opensmartgridplatform.dto.valueobjects.DeviceRegistrationDataDto
import org.opensmartgridplatform.dto.valueobjects.EventNotificationDto
import org.opensmartgridplatform.shared.infra.jms.RequestMessage
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import java.io.Serializable

@ExtendWith(MockKExtension::class)
@ExtendWith(OutputCaptureExtension::class)
class DeviceEventMessageSenderTest {
    @MockK
    lateinit var jmsTemplate: JmsTemplate

    @MockK
    lateinit var properties: DeviceEventsConfigurationProperties

    @InjectMockKs
    lateinit var deviceEventMessageSender: DeviceEventMessageSender

    private lateinit var objectMessage: ObjectMessage
    private lateinit var session: Session
    private lateinit var message: DeviceEventMessage
    private lateinit var requestMessage: RequestMessage

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
    fun `should send device registration event object message`() {
        // Arrange
        message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION)

        // Act
        deviceEventMessageSender.send(message)

        // Assert
        verifyObjectMessageIsSent()
        verifyObjectMessageProperties()
        verifyObjectMessageContainsRequestMessage()
        verifyRequestMessageContainsRegistrationDataDto()
    }

    @Test
    fun `should send device registration confirmation event object message`() {
        // Arrange
        message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_REGISTRATION_CONFIRMATION)

        // Act
        deviceEventMessageSender.send(message)

        // Assert
        verifyObjectMessageIsSent()
        verifyObjectMessageProperties()
        verifyObjectMessageContainsRequestMessage()
        assertThat(requestMessage.request).isNull()
    }

    @Test
    fun `should send device notification event object message`() {
        // Arrange
        message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.DEVICE_NOTIFICATION)

        // Act
        deviceEventMessageSender.send(message)

        // Assert
        verifyObjectMessageIsSent()
        verifyObjectMessageProperties()
        verifyObjectMessageContainsRequestMessage()
        verifyRequestMessageContainsEventNotificationDto()
    }

    @Test
    fun `should log unrecognized protobuf event and not send dto`(capturedOutput: CapturedOutput) {
        // Arrange
        message = DeviceEventMessageFactory.protobufMessageForEventOfType(EventType.UNRECOGNIZED)

        // Act
        deviceEventMessageSender.send(message)

        // Assert
        assertThat(capturedOutput.out)
            .contains("Failed to send device event message")
            .contains("Unsupported event type: UNRECOGNIZED")
    }

    private fun verifyObjectMessageIsSent() {
        var creator: MessageCreator? = null
        verify { jmsTemplate.send(OUTBOUND_QUEUE_NAME, withArg { creator = it }) }
        val message = creator!!.createMessage(session)
        assertThat(message).isInstanceOf(ObjectMessage::class.java).isEqualTo(objectMessage)
    }

    private fun verifyObjectMessageProperties() {
        verify { objectMessage.jmsType = message.header.eventType.toMessageType() }
        verify { objectMessage.jmsCorrelationID = message.header.correlationUid }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, message.header.deviceIdentification) }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, message.header.organizationIdentification) }
    }

    private fun verifyObjectMessageContainsRequestMessage() {
        var requestMessage: RequestMessage? = null
        verify { objectMessage.`object` = withArg<Serializable> { requestMessage = it as RequestMessage } }
        assertThat(requestMessage).isNotNull()
        this.requestMessage = requestMessage!!
    }

    private fun verifyRequestMessageContainsRegistrationDataDto() {
        with(requestMessage) {
            assertThat(correlationUid).isEqualTo(message.header.correlationUid)
            assertThat(deviceIdentification).isEqualTo(message.header.deviceIdentification)
            assertThat(organisationIdentification).isEqualTo(message.header.organizationIdentification)
            assertThat(request).isNotNull().isInstanceOf(DeviceRegistrationDataDto::class.java)
            with(request as DeviceRegistrationDataDto) {
                assertThat(ipAddress).isEqualTo(message.deviceRegistrationReceivedEvent.networkAddress)
                assertThat(deviceType).isEqualTo("SSLD")
                assertThat(hasSchedule).isEqualTo(message.deviceRegistrationReceivedEvent.hasSchedule)
            }
        }
    }

    private fun verifyRequestMessageContainsEventNotificationDto() {
        with(requestMessage) {
            assertThat(correlationUid).isEqualTo(message.header.correlationUid)
            assertThat(deviceIdentification).isEqualTo(message.header.deviceIdentification)
            assertThat(organisationIdentification).isEqualTo(message.header.organizationIdentification)
            assertThat(request).isNotNull().isInstanceOf(EventNotificationDto::class.java)
        }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "queue"
    }
}
