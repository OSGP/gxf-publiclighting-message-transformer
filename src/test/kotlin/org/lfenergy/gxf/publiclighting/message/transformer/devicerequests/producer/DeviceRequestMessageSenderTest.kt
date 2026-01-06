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
import org.lfenergy.gxf.publiclighting.message.transformer.common.TestConstants.DEVICE_IDENTIFICATION
import org.lfenergy.gxf.publiclighting.message.transformer.devicerequests.OutboundRequestMessageFactory
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
    fun `should send protobuf bytes message for get configuration request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.GET_CONFIGURATION_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for get firmware version request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.GET_FIRMWARE_VERSION_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for get status request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.GET_STATUS_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for reboot request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.REBOOT_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for set configuration request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.SET_CONFIGURATION_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for resume schedule request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.RESUME_SCHEDULE_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for set event notifications request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for set light request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.SET_LIGHT_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for set schedule request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.SET_SCHEDULE_REQUEST)
    }

    @Test
    fun `should send protobuf bytes message for set transition request`() {
        verifyBytesMessageIsSentForRequestType(RequestType.SET_TRANSITION_REQUEST)
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
        val message = OutboundRequestMessageFactory.deviceRequestMessage(requestType)

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
                    when (requestType) {
                        RequestType.RESUME_SCHEDULE_REQUEST -> assertThat(deviceRequestMessage.hasResumeScheduleRequest()).isTrue
                        RequestType.SET_CONFIGURATION_REQUEST -> assertThat(deviceRequestMessage.hasSetConfigurationRequest()).isTrue
                        RequestType.SET_LIGHT_REQUEST -> assertThat(deviceRequestMessage.hasSetLightRequest()).isTrue
                        RequestType.SET_EVENT_NOTIFICATION_MASK_REQUEST ->
                            assertThat(
                                deviceRequestMessage.hasSetEventNotificationMaskRequest(),
                            ).isTrue
                        RequestType.SET_SCHEDULE_REQUEST -> assertThat(deviceRequestMessage.hasSetScheduleRequest()).isTrue
                        RequestType.SET_TRANSITION_REQUEST -> assertThat(deviceRequestMessage.hasSetTransitionRequest()).isTrue
                        else -> { /* no payload */ }
                    }
                },
            )
        }
    }

    companion object {
        private const val OUTBOUND_QUEUE_NAME = "queue"
    }
}
