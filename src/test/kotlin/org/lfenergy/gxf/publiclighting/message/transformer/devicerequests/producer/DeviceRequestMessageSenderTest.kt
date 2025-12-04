// SPDX-FileCopyrightText: Copyright Contributors to the GXF project
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.producer

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import jakarta.jms.BytesMessage
import jakarta.jms.Session
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.DeviceRequestMessage
import org.lfenergy.gxf.publiclighting.contracts.internal.device_requests.RequestType
import org.lfenergy.gxf.publiclighting.message.transformer.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.DeviceRequestMessageFactory
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.config.DeviceRequestsConfigurationProperties
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

@ExtendWith(MockKExtension::class)
class DeviceRequestMessageSenderTest {
    @MockK
    lateinit var jmsTemplate: JmsTemplate

    @MockK
    lateinit var properties: DeviceRequestsConfigurationProperties

    @InjectMockKs
    lateinit var deviceRequestMessageSender: DeviceRequestMessageSender

    private lateinit var bytesMessage: BytesMessage
    private lateinit var session: Session

    @BeforeEach
    fun setupMocks() {
        bytesMessage = mockk(relaxed = true)
        session = mockk(relaxed = true)

        every { properties.producer.outboundQueue } returns OUTBOUND_QUEUE_NAME
        every { session.createBytesMessage() } returns bytesMessage
        every { jmsTemplate.send(any<String>(), any<MessageCreator>()) } just runs
    }

    @Test
    fun `should send protobuf bytes message for get status request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.GET_STATUS_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for set light request`() {
        // Arrange
        val message = DeviceRequestMessageFactory.deviceRequestMessage(RequestType.SET_LIGHT_REQUEST)

        // Act
        deviceRequestMessageSender.send(message)

        // Assert
        verifyBytesMessageIsSent()
        verifyBytesMessageContainsProtobufMessage(RequestType.SET_LIGHT_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for reboot request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.REBOOT_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for start self test request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.START_SELF_TEST_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for stop self test request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.STOP_SELF_TEST_REQUEST)
    }

    fun verifyBytesMessageIsSentForRequestType(requestType: RequestType) {
        val message = DeviceRequestMessageFactory.deviceRequestMessage(requestType)

        deviceRequestMessageSender.send(message)

        verifyBytesMessageIsSent()
        verifyBytesMessageContainsProtobufMessage(requestType)
    }

    private fun verifyBytesMessageIsSent() {
        var creator: MessageCreator? = null
        verify { jmsTemplate.send(OUTBOUND_QUEUE_NAME, withArg { creator = it }) }
        val message = creator!!.createMessage(session)
        assertThat(message).isInstanceOf(BytesMessage::class.java).isEqualTo(bytesMessage)
    }

    private fun verifyBytesMessageContainsProtobufMessage(requestType: RequestType) {
        verify {
            bytesMessage.writeBytes(
                withArg { bytes ->
                    val deviceRequestMessage = DeviceRequestMessage.parseFrom(bytes)
                    assertThat(deviceRequestMessage.header.deviceIdentification).isEqualTo(DEVICE_IDENTIFICATION)
                    assertThat(deviceRequestMessage.header.requestType).isEqualTo(requestType)
                },
            )
        }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "queue"
    }
}
