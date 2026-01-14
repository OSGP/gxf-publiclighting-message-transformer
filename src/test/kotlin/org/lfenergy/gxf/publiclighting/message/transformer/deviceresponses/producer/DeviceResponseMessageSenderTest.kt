// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.producer

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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.DeviceResponseMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_responses.ResponseType
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.common.ApplicationConstants.JMS_PROPERTY_ORGANIZATION_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.DeviceResponseTestHelper.payloadPerResponseTypeAssertions
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.InboundResponseMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.config.DeviceResponsesConfigurationProperties
import org.lfenergy.gxf.publiclighting.message.transformer.deviceresponses.mapper.DeviceResponseMessageMapper.toMessageType
import org.opensmartgridplatform.shared.infra.jms.ProtocolResponseMessage
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import java.io.Serializable

@ExtendWith(MockKExtension::class)
@ExtendWith(OutputCaptureExtension::class)
class DeviceResponseMessageSenderTest {
    @MockK
    lateinit var jmsTemplate: JmsTemplate

    @MockK
    lateinit var properties: DeviceResponsesConfigurationProperties

    @InjectMockKs
    lateinit var deviceResponseMessageSender: DeviceResponseMessageSender

    private lateinit var objectMessage: ObjectMessage
    private lateinit var session: Session
    private lateinit var message: DeviceResponseMessage
    private lateinit var protocolResponseMessage: ProtocolResponseMessage

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

    @ParameterizedTest(name = "with response type {0}")
    @MethodSource("responseTypeProvider")
    fun `should send protocol response message`(responseType: ResponseType) {
        message = InboundResponseMessageFactory.protobufMessageForResponseOfType(responseType)

        deviceResponseMessageSender.send(message)

        verifyObjectMessage()
        verifyProtocolResponseMessage(responseType)
    }

    @Test
    fun `should log unrecognized protobuf event and not send dto`(capturedOutput: CapturedOutput) {
        // Arrange
        message = InboundResponseMessageFactory.protobufMessageForResponseOfType(ResponseType.UNRECOGNIZED)

        // Act
        deviceResponseMessageSender.send(message)

        // Assert
        assertThat(capturedOutput.out)
            .contains("Failed to send device response message")
            .contains("Unsupported message type: UNRECOGNIZED")
    }

    private fun verifyObjectMessage() {
        verifyObjectMessageIsSent()
        verifyObjectMessageProperties()
        verifyObjectMessageContainsProtocolResponseMessage()
    }

    private fun verifyObjectMessageIsSent() {
        var creator: MessageCreator? = null
        verify { jmsTemplate.send(OUTBOUND_QUEUE_NAME, withArg { creator = it }) }
        val message = creator!!.createMessage(session)
        assertThat(message).isInstanceOf(ObjectMessage::class.java).isEqualTo(objectMessage)
    }

    private fun verifyObjectMessageProperties() {
        verify { objectMessage.jmsType = message.header.responseType.toMessageType() }
        verify { objectMessage.jmsCorrelationID = message.header.correlationUid }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_DEVICE_IDENTIFICATION, message.header.deviceIdentification) }
        verify { objectMessage.setStringProperty(JMS_PROPERTY_ORGANIZATION_IDENTIFICATION, message.header.organizationIdentification) }
    }

    private fun verifyObjectMessageContainsProtocolResponseMessage() {
        var responseMessage: ProtocolResponseMessage? = null
        verify { objectMessage.`object` = withArg<Serializable> { responseMessage = it as ProtocolResponseMessage } }
        assertThat(responseMessage).isNotNull()
        this.protocolResponseMessage = responseMessage!!
    }

    private fun verifyProtocolResponseMessage(responseType: ResponseType) {
        with(protocolResponseMessage) {
            assertThat(correlationUid).isEqualTo(message.header.correlationUid)
            assertThat(deviceIdentification).isEqualTo(message.header.deviceIdentification)
            assertThat(organisationIdentification).isEqualTo(message.header.organizationIdentification)
            assertThat(messageType).isEqualTo(responseType.toMessageType())
            assertThat(result).isEqualTo(ResponseMessageResultType.OK)

            payloadPerResponseTypeAssertions[responseType]?.invoke(this)
        }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "queue"

        @JvmStatic
        fun responseTypeProvider() =
            ResponseType.entries
                .filterNot { it == ResponseType.UNRECOGNIZED }
                .map { Arguments.of(it) }
                .stream()
    }
}
